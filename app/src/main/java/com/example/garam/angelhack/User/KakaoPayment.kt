package com.example.garam.angelhack.User

import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.KakaoApi
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.content.ContextCompat.startActivity
import org.json.JSONObject

class KakaoPayment : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
    init {
        instance = this
    }
    companion object{
        private var instance: KakaoPayment? = null
        fun context(): Context {
            return instance!!.applicationContext
        }
    }


    private var qrCodeReaderView: QRCodeReaderView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_payment)
        val qrCodeReaderView = findViewById<QRCodeReaderView>(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this)

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true)

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L)
        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true)

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera()

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera()
    }

    override fun onQRCodeRead(text: String?, points: Array<PointF?>?) {
        Log.e("텍스트","$text")
        val resultText = findViewById<TextView>(R.id.resultQR)
        if (resultText.text.isNotEmpty()){
            finish()
        }
        else {
            resultText.text = text
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
                30000,
                0,
                "https://5fd8fbe51d8f.ngrok.io",
                "https://5fd8fbe51d8f.ngrok.io",
                "https://5fd8fbe51d8f.ngrok.io"
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
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url3))
                    val intent = Intent(Intent.ACTION_VIEW)
   //                 intent.setPackage("com.android.chrome")
                    intent.data = Uri.parse(url2.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    startActivity(context(),intent,null)
                   // val intent = Intent(this@KakaoPayment, KakaoWeb::class.java)
                   // intent.putExtra("url", url3)
                   // startActivity(intent)
                  //  finish()
                  //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                   // startActivity(intent)
                 //   startActivity(baseContext,intent,null)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("로그", "${t.message}")
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