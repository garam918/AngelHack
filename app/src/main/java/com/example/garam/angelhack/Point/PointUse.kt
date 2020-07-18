package com.example.garam.angelhack.Point

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garam.angelhack.R
import com.example.garam.angelhack.User.UserMenu
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_point_use.*
import kotlinx.android.synthetic.main.activity_pre_payinfo.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PointUse : AppCompatActivity() {

    val baseURL = "https://dfcb69ae67f1.ngrok.io"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(
        GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit2.create(NetworkService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_use)

        val intent = intent
        val uid = intent.getStringExtra("uid")
        val hid = intent.getStringExtra("hid")
        val obj = JSONObject()


        homeButton2.setOnClickListener {
            homeButton.setOnClickListener {
                val intent = Intent(this, UserMenu::class.java)
                intent.putExtra("uid",uid)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }

        PointUseButton.setOnClickListener {
            obj.put("uid",uid)
            obj.put("hid",hid)
            obj.put("used_money",pointEdit.text)
            val json = obj.toString()
            val gsonObject = JsonParser().parse(json) as JsonObject
            val pointuse : Call<JsonObject> = networkService.pointSend(gsonObject)
            pointuse.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val obj = JSONObject(response.body().toString())
                    val remain = obj.getInt("money")
                    val nextIntent = Intent(this@PointUse,PointUseFininsh::class.java)
                    nextIntent.putExtra("remain_money",remain)
                    startActivity(nextIntent)
                }
            })
        }
    }
}