package com.example.garam.angelhack.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PrePayinfo : AppCompatActivity() {


    val baseURL = "https://80fac4eb1b11.ngrok.io"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(
        GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit2.create(NetworkService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_payinfo)


        val hid = intent.getStringExtra("hid")
        val uid = intent.getStringExtra("uid")
        val money = intent.getIntExtra("money",0)


        val obj = JSONObject()
        obj.put("hid",hid)
        obj.put("uid",uid)
        obj.put("money",money)
        val json = obj.toString()
        Log.e("payment", json)
        val gsonObject = JsonParser().parse(json) as JsonObject
        val paymentSave = networkService.paymentSave(gsonObject)
        paymentSave.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.e("리스폰스", "${response.body()}")
            }
        })


    }
}