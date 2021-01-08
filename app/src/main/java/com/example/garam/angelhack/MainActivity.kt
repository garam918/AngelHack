package com.example.garam.angelhack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.angelhack.Manager.HelloManager
import com.example.garam.angelhack.Manager.ManageMenu
import com.example.garam.angelhack.User.UserMenu
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session.getCurrentSession
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val baseURL = "http://15.165.205.48:8000"

    val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).client(OkHttpClient.Builder().connectTimeout(1,
        TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(HttpLoggingInterceptor()).build()).build()
    val networkService = retrofit.create(NetworkService::class.java)
    var flag = 1000
    private var callback: SessionCallback = SessionCallback()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = findViewById<Button>(R.id.btn_kakao_manager_login)
        manager.setOnClickListener {
               getCurrentSession().addCallback(callback)
               getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
               flag = 1
        }

        val user = findViewById<Button>(R.id.btn_kakao_user_login)
        user.setOnClickListener {
            getCurrentSession().addCallback(callback)
            getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
            flag = 0
        }

        AuthService.getInstance()
            .requestAccessTokenInfo(object : ApiResponseCallback<AccessTokenInfoResponse?>() {
                override fun onSessionClosed(errorResult: ErrorResult) {
                }

                override fun onFailure(errorResult: ErrorResult) {
                }

                override fun onSuccess(result: AccessTokenInfoResponse?) {
                }
            })

    }
    override fun onDestroy() {
        super.onDestroy()
        getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    inner class SessionCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {

                }

                override fun onSuccess(result: MeV2Response?) {

                    val kakaoAccount = result!!.kakaoAccount
                    val email = kakaoAccount.email
                    val profile = kakaoAccount.profile
                    val obj = JSONObject()
                    obj.put("uid",result.id)
                    obj.put("name",profile.nickname)
                    val json = obj.toString()
                    val userInfo = JsonParser().parse(json) as JsonObject
                    sendInfo(userInfo)
                    checkNotNull(result) { "session response null" }

                    redirectSignup("${result.id}","${profile.nickname}")
                }

            })
        }

    }

    fun sendInfo(userInfo: JsonObject){

        val userlogin : Call<Void> = networkService.userlogin(userInfo)
        userlogin.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
    }

    fun redirectSignup(uid: String,name : String){

        if (flag == 0) {
            val intent = Intent(this, UserMenu::class.java)
            intent.putExtra("uid", uid)
            intent.putExtra("name",name)
            startActivity(intent)
        }
        else if (flag == 1) {

            val obj = JSONObject()
            obj.put("uid",uid)
            val json = obj.toString()
            val gsonObject = JsonParser().parse(json) as JsonObject

            val hostcheck : Call<JsonObject> = networkService.hostcheck(gsonObject)
            hostcheck.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val res = response.body()!!
                    if (res.get("storename").toString() == "\"nothing\""){
                        val manageintent = Intent(this@MainActivity,ManageMenu::class.java)
                        manageintent.putExtra("hid",uid)
                        startActivity(manageintent)
                    }
                    else {
                        val hellointent = Intent(this@MainActivity, HelloManager::class.java)
                        hellointent.putExtra("hid", uid)
                        startActivity(hellointent)
                    }
                }
            })
        }
    }
}