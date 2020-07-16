package com.example.garam.angelhack.User

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.KakaoApi
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

class StoreInfo : AppCompatActivity() {

    val baseURL = "https://80fac4eb1b11.ngrok.io"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit2.create(NetworkService::class.java)
    lateinit var money : String
    lateinit var hid : String
    lateinit var uid : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info)

       // val moneyInfo = intent.getStringExtra("money")

        val intent = intent
        money = intent.getStringExtra("money")
        hid = intent.getStringExtra("hid")
        uid = intent.getStringExtra("uid")
        val kPayment = findViewById<Button>(R.id.kakaoPay)

        kPayment.setOnClickListener {

            val intent = Intent(this,PayAmount::class.java)
            startActivityForResult(intent,100)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) {
            val money = data?.getStringExtra("amount")
            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
            val networkService = retrofit.create(NetworkService::class.java)
            val pay: Call<JsonObject> = networkService.payments(
                "${KakaoApi.instance.key}",
                "TC0ONETIME",
                "6406"
                ,
                "pg_qa",
                "초코파이",
                1,
                money?.toInt()!!,
                0,
                "https://80fac4eb1b11.ngrok.io",
                "https://80fac4eb1b11.ngrok.io",
                "https://80fac4eb1b11.ngrok.io"
            )

            pay.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("로그", "${response.body()}")
                    val res = response.body()!!
                    val kakao = JSONObject(res.toString())
                    val url = "${res.get("android_app_scheme")}"
                    val url2 = kakao.getString("next_redirect_app_url")
                    val url3 = res.get("next_redirect_mobile_url")
                    Log.e("URL1",url)
                    Log.e("URL2","$url2")
                    Log.e("URL3","$url3")
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url2.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                   // intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                   // ContextCompat.startActivity(KakaoPayment.context(), intent, null)
                    startActivityForResult(intent,1000)

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("로그", "${t.message}")
                }
            })

        }
        else if ( resultCode == Activity.RESULT_OK){
            val obj = JSONObject()
            obj.put("hid",hid)
            obj.put("uid",uid)
            obj.put("money",money.toInt())
            val json = obj.toString()
            Log.e("payment", json)
            val gsonObject = JsonParser().parse(json) as JsonObject
            val paymentSave = networkService.paymentSave(gsonObject)
            paymentSave.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("리스폰스", "${response.body()}")
                }
            })
        }
    }
}