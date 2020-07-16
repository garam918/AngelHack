package com.example.garam.angelhack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.angelhack.User.UserMenu
import com.example.garam.angelhack.network.ApplicationController
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session.getCurrentSession
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
/*
    private val networkService : NetworkService by lazy {
        ApplicationController.instance.networkService
    }
  */
    private var callback: SessionCallback = SessionCallback()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentSession().addCallback(callback)

    }
    override fun onDestroy() {
        super.onDestroy()
        getCurrentSession().removeCallback(callback);
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

                    redirectSignup()
                }

            })
        }

    }

    fun sendInfo(userInfo: JsonObject){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(ApplicationController.instance.baseURL).addConverterFactory(GsonConverterFactory.create()).client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()).build()

        val networkService = retrofit.create(NetworkService::class.java)

        val userlogin : Call<JsonObject> = networkService.userlogin(userInfo)
        userlogin.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                Log.e("로그","성공")
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("로그","실패")
            }

        })
    }

    fun redirectSignup(){
        val intent = Intent(this,UserMenu::class.java)
        startActivity(intent)
    }
}