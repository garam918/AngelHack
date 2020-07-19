package com.example.garam.angelhack.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.garam.angelhack.MainActivity
import com.example.garam.angelhack.Point.PointQr
import com.example.garam.angelhack.Point.PointUse
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
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


class UserMenu : AppCompatActivity() {

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
        setContentView(R.layout.activity_user_menu)
        val uid = intent.getStringExtra("uid")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        payment.setOnClickListener {
            val qrpay = Intent(this,KakaoPayment::class.java)
            qrpay.putExtra("uid",uid)
            startActivity(qrpay)
        }

        pointUse.setOnClickListener {
            val qrPoint = Intent(this,PointQr::class.java)
            qrPoint.putExtra("uid",uid)
            startActivity(qrPoint)
        }

        logout.setOnClickListener {
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
                        val intent = Intent(this@UserMenu,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    override fun onNotSignedUp() {
                        Log.e("kakao","가입안되있음")
                        super.onNotSignedUp()
                    }
                })
        }
        storeList.setOnClickListener {
            val obj = JSONObject()
            obj.put("uid",uid)

            val json = JsonParser().parse(obj.toString()) as JsonObject
            val polist : Call<JsonArray> = networkService.payMentList(json)
            polist.enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.e("실패","${t.message}")
                    Toast.makeText(this@UserMenu,"결제 내역이 없습니다", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    Log.e("결제 내역","${response.body()}")

                    val intent = Intent(this@UserMenu,PointList::class.java)
                    intent.putExtra("","")
                    startActivity(intent)
                }
            })
        }
    }
}