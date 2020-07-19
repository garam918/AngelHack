package com.example.garam.angelhack.Manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.garam.angelhack.MainActivity
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.activity_hello_manager.*
import kotlinx.android.synthetic.main.activity_user_menu.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HelloManager : AppCompatActivity() {

    val baseURL = "http://15.165.205.48:8000"
    val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(
        GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
        TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit.create(NetworkService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_manager)
        val intent = intent
        val hid = intent.getStringExtra("hid")
        val obj = JSONObject()
        obj.put("hid",hid)
        val json = obj.toString()
        val gsonObject = JsonParser().parse(json) as JsonObject

        Log.e("hid",hid)
        userPayList.setOnClickListener {
            val post : Call<JsonObject> = networkService.chargePoint(gsonObject)
            post.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("실패","${t.message}")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("결제 목록","${response.body()}")
                    val res = response.body()!!
                    if(res.get("name").toString() == "\"nothing\""){
                        Toast.makeText(this@HelloManager,"결제 내역이 없습니다",Toast.LENGTH_LONG).show()
                    }
                    else {
                        val intent = Intent(this@HelloManager,UserPayForManage::class.java)
                        startActivity(intent)
                    }
                }
            })
        }
        pointList.setOnClickListener {
            val sonnim : Call<JsonArray> = networkService.payList(gsonObject)
            sonnim.enqueue(object : Callback<JsonArray>{
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.e("실패","${t.message}")
                }

                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    Log.e("결제 손님 리스트","${response.body()}")
                }
            })
        }
        logout2.setOnClickListener {

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
                        val intent = Intent(this@HelloManager, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    override fun onNotSignedUp() {
                        Log.e("kakao","가입안되있음")
                        super.onNotSignedUp()
                    }
                })
        }
        manageMypage.setOnClickListener {
            val intent = Intent(this,ManagePage::class.java)
            intent.putExtra("hid",hid)
            intent.putExtra("","")
            intent.putExtra("","")
            startActivity(intent)
        }
    }
}