package com.example.garam.angelhack.User

import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
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

class KakaoPayment : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {

    val baseURL = "http://15.165.205.48:8000"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
        TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit2.create(NetworkService::class.java)
    lateinit var uid: String
    private var qrCodeReaderView: QRCodeReaderView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_payment)
        val intent = intent
        uid = intent.getStringExtra("uid")

        val qrCodeReaderView = findViewById<QRCodeReaderView>(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this)
        qrCodeReaderView.setQRDecodingEnabled(true)
        qrCodeReaderView.setAutofocusInterval(2000L)
        qrCodeReaderView.setTorchEnabled(true)
        qrCodeReaderView.setFrontCamera()
        qrCodeReaderView.setBackCamera()
    }

    override fun onQRCodeRead(text: String?, points: Array<PointF?>?) {
        lateinit var money : String
        val hostname = JSONObject(text.toString())
        val hostInfoname = hostname.get("storename").toString()
        val hostTextView = findViewById<TextView>(R.id.hostNameInfo)
        val jsonObject = JSONObject()
        jsonObject.put("uid",uid)
        jsonObject.put("hid",hostname.get("uid"))
        jsonObject.put("storename",hostInfoname)
        jsonObject.put("introduceText",hostname.get("introduceText").toString())
        val json = jsonObject.toString()
        if (hostTextView.text.isNotEmpty()){
            finish()
        }
        else {
            Log.e("텍스트","$text")
            hostTextView.text = hostInfoname
            val obj  = JsonParser().parse(json) as JsonObject
            Log.e("fadf","$obj")
            Log.e("fadf","${obj.asJsonObject}")
            val payInfo = networkService.prePayment(obj.asJsonObject)
            payInfo.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("머니","$t")
                }
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("머니","${response.body()}")
                    money = response.body()!!.get("money").toString()
                    val hid = obj.get("hid").asString
                    val intent = Intent(this@KakaoPayment,StoreInfo::class.java)
                    intent.putExtra("money",money)
                    intent.putExtra("hid",hid)
                    intent.putExtra("uid",uid)
                    startActivity(intent)

                }

            })
        }
    }


    override fun onResume() {
        super.onResume()
        if(qrCodeReaderView!=null){
        this.qrCodeReaderView!!.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
       // this.qrCodeReaderView!!.stopCamera()
    }
}