package com.example.garam.angelhack.User

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.KakaoApi
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_store_info.*
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

    val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    val networkService = retrofit.create(NetworkService::class.java)

    val baseURL = "http://15.165.205.48:8000"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService2 = retrofit2.create(NetworkService::class.java)
    lateinit var money : String
    lateinit var hid : String
    lateinit var uid : String
    lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info)

        val intent = intent
        money = intent.getStringExtra("money")
        hid = intent.getStringExtra("hid")
        uid = intent.getStringExtra("uid")
        name = intent.getStringExtra("name")
        val store = intent.getStringExtra("storename")
        val introduce = intent.getStringExtra("introduceText")

        storeName.text = store
        StoreInfo.text = introduce

        val kPayment = findViewById<Button>(R.id.kakaoPay)

        kPayment.setOnClickListener {

            val intent = Intent(this,PayAmount::class.java)
            startActivityForResult(intent,100)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data?.getStringExtra("tid") != null) {
            val money = data?.getStringExtra("amount")?.toInt()
            val tid = data?.getStringExtra("tid").toString()
            val check : Call<JsonObject> = networkService.payCheck("${KakaoApi.instance.key}",
                "TC0ONETIME",tid)
            check.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val res = response.body()!!
                    val kakao = JSONObject(res.toString())
                    if (kakao.getString("status") == "AUTH_PASSWORD"){
                        val intent = Intent(this@StoreInfo, PrePayinfo::class.java)
                        intent.putExtra("hid", hid)
                        intent.putExtra("uid", uid)
                        intent.putExtra("name",name)
                        intent.putExtra("money", money)

                        startActivity(intent)
                    }
                }
            })
        }
    }
}