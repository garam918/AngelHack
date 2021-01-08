package com.example.garam.angelhack.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.garam.angelhack.MainActivity
import com.example.garam.angelhack.Point.PointQr
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import kotlinx.android.synthetic.main.activity_user_menu.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
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
        val name = intent.getStringExtra("name")
        userInfo.text = "안녕하세요. \n $name 님"
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
            qrpay.putExtra("name",name)
            startActivity(qrpay)
        }

        pointUse.setOnClickListener {
            val qrPoint = Intent(this,PointQr::class.java)
            qrPoint.putExtra("uid",uid)
            qrPoint.putExtra("name",name)
            startActivity(qrPoint)
        }

        logout.setOnClickListener {
            UserManagement.getInstance()
                .requestUnlink(object : UnLinkResponseCallback() {
                    override fun onSessionClosed(errorResult: ErrorResult) {
                    }

                    override fun onFailure(errorResult: ErrorResult) {
                    }

                    override fun onSuccess(result: Long) {
                        val intent = Intent(this@UserMenu,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    override fun onNotSignedUp() {
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
                    Toast.makeText(this@UserMenu,"결제 내역이 없습니다", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val res = JSONArray(response.body().toString()).getString(0)
                    val respon = JSONObject(res.toString())
                    val intent = Intent(this@UserMenu,MyPage::class.java)
                    intent.putExtra("name",name)
                    intent.putExtra("storename",respon.getString("storename"))
                    intent.putExtra("money",respon.getString("money"))
                    startActivity(intent)
                }
            })
        }
    }
}