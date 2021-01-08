package com.example.garam.angelhack.User

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doBeforeTextChanged
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.KakaoApi
import com.example.garam.angelhack.network.NetworkService
import com.example.garam.angelhack.urlData
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PayAmount : AppCompatActivity() {
    lateinit var urlData : urlData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_amount)

        val serverUrl = "http://15.165.205.48:8000"
        val payedit = findViewById<EditText>(R.id.payEdit)
        val paybutton = findViewById<Button>(R.id.payButton)
        paybutton.isEnabled = false
        payedit.doBeforeTextChanged { text, start, count, after ->
              paybutton.isEnabled = true
        }
        paybutton.setOnClickListener {
            val data = intent
            data.putExtra("amount","${payedit.text}")
            val money = payedit.text.toString()
            var moneyInfo = money.toInt()
            val retrofit: Retrofit =
                Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
                    GsonConverterFactory.create()
                ).build()
            val networkService = retrofit.create(NetworkService::class.java)
            val pay: Call<JsonObject> = networkService.payments(
                "${KakaoApi.instance.key}",
                "TC0ONETIME",
                "6406",
                "pg_qa",
                "초코파이",
                1,
                moneyInfo,
                0,
                serverUrl,
                serverUrl,
                serverUrl
            )
            pay.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val res = response.body()!!
                    val kakao = JSONObject(res.toString())
                    val url2 = kakao.getString("next_redirect_app_url")
                    val tid = kakao.getString("tid")
                    urlData = urlData(tid,url2)
                    data.putExtra("tid",tid)

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url2.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            })
            setResult(100,data)


        }

    }
}