package com.example.garam.angelhack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
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
import com.kakao.usermgmt.callback.LogoutResponseCallback
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
    val baseURL = "https://a961f35ba588.ngrok.io"
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
                    Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
                }

                override fun onFailure(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "토큰 정보 요청 실패: $errorResult")
                }

                override fun onSuccess(result: AccessTokenInfoResponse?) {
                    Log.i("KAKAO_API", "사용자 아이디: " + result?.userId)
                    Log.i("KAKAO_API", "남은 시간(s): " + result?.expiresInMillis)
                }
            })

/*
        UserManagement.getInstance()
            .requestUnlink(object : UnLinkResponseCallback() {
                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
                }

                override fun onFailure(errorResult: ErrorResult) {
                    Log.e("KAKAO_API", "연결 끊기 실패: $errorResult")
                }

                override fun onSuccess(result: Long) {
                    Log.i("KAKAO_API", "연결 끊기 성공. id: $result")
                }

                override fun onNotSignedUp() {
                    Log.e("kakao","가입안되있음")
                    super.onNotSignedUp()
                }
            })*/
    }
    override fun onDestroy() {
        super.onDestroy()
        getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.e("Log", "session get current session")

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    inner class SessionCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("Log", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("Log", "Session Call back :: on failed ${errorResult?.errorMessage}")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e("Log", "Session Call back :: onSessionClosed ${errorResult?.errorMessage}")

                }

                override fun onSuccess(result: MeV2Response?) {

                    val kakaoAccount = result!!.kakaoAccount
                    Log.e("Log","결과값 :$result")
                    val email = kakaoAccount.email
                    val profile = kakaoAccount.profile
                    Log.e("Log","아이디 : ${result.id}")
                    Log.e("Log", "이름 : ${profile.nickname}")
                    Log.e("Log", "이메일 : $email")
                    Log.e("Log", "프로필 이미지 : ${profile.profileImageUrl}")
                    val obj = JSONObject()
                    obj.put("uid",result.id)
                    obj.put("name",profile.nickname)
                    val json = obj.toString()
                    val userInfo = JsonParser().parse(json) as JsonObject
                    sendInfo(userInfo)
                    checkNotNull(result) { "session response null" }

                    redirectSignup("${result.id}")
                }

            })
        }

    }

    fun sendInfo(userInfo: JsonObject){

        val userlogin : Call<JsonObject> = networkService.userlogin(userInfo)
        userlogin.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                Log.e("로그","$response")
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("로그","실패")
            }

        })
    }

    fun redirectSignup(uid: String){

        Log.e("플래그","$flag")

        if (flag == 0) {
            val intent = Intent(this, UserMenu::class.java)
            intent.putExtra("uid", uid)
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
                    Log.e("유저 체크","${response.body()}")
                    val res = response.body()!!
                    Log.e("유저 확인",res.get("storename").toString())
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