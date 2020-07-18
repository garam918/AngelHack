package com.example.garam.angelhack.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_hello_manager.*
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

    val baseURL = "https://dfcb69ae67f1.ngrok.io"
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

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("결제 목록","${response.body()}")
                    val res = response.body()!!
                    if(res.get("msg").toString() == "\"nothing\""){
                        Toast.makeText(this@HelloManager,"결제 내역이 없습니다",Toast.LENGTH_LONG).show()
                    }
                    else {
                        
                    }
                }
            })
        }
    }
}