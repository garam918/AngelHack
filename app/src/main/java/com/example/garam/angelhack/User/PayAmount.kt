package com.example.garam.angelhack.User

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
                "https://a961f35ba588.ngrok.io",
                "https://a961f35ba588.ngrok.io",
                "https://a961f35ba588.ngrok.io"
            )
            pay.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("로그", "${response.body()}")
                    val res = response.body()!!
                    val kakao = JSONObject(res.toString())
                    val url = "${res.get("android_app_scheme")}"
                    val url2 = kakao.getString("next_redirect_app_url")
                    val url3 = kakao.getString("next_redirect_mobile_url")
                    val tid = kakao.getString("tid")
                    urlData = urlData(tid,url2)
                    data.putExtra("tid",tid)
                    Log.e("tid",tid)
                    Log.e("URL1",url)
                    Log.e("URL2","$url2")
                    Log.e("URL3","$url3")

                    /*   val intent = Intent(this@StoreInfo,KakaoWebview::class.java)
                       intent.putExtra("url",url2)
                       startActivity(intent)
   */
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url2.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    // intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    // ContextCompat.startActivity(KakaoPayment.context(), intent, null)
                    startActivity(intent)
                    Log.e("기록1","아아")
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("로그", "${t.message}")
                }
            })
            setResult(100,data)
            Log.e("테스트","??????????????????")

 //            finish()

        }

    }
}