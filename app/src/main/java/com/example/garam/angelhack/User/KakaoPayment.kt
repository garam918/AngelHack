package com.example.garam.angelhack.User

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

class KakaoPayment : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
//    private val resultTextView: TextView? = null
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
        resultText.text = text
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.base).addConverterFactory(
            GsonConverterFactory.create()).build()
        val networkService = retrofit.create(NetworkService::class.java)
        val pay: Call<JsonObject> = networkService.payments("${KakaoApi.instance.key}","TC0ONETIME","6406"
        ,"pg_qa","초코파이",1,30000,0,"https://e75de2626c4a.ngrok.io","https://e75de2626c4a.ngrok.io","https://e75de2626c4a.ngrok.io")
        pay.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                 Log.e("로그","${response.body()}")
                 val res = response.body()
                 val url = "${res?.get("android_app_scheme")}"
                 val url2 = "${res?.get("next_redirect_app_url")}"
                 Log.e("URL", "$url")
                 Log.e("URL", "$url2")
              //   val intent = Intent.parseUri(url.toString(),Intent.URI_INTENT_SCHEME)
              //   val packageintent = packageManager.getLaunchIntentForPackage(intent.`package`!!)
                 //intent.data = Uri.parse("$url")
                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // if(packageintent!=null) startActivity(intent)
              //   startActivity(this@KakaoPayment,intent,null)
                 redirect(url2)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("로그", "${t.message}")
            }
        })
    }
    fun redirect(url: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(this@KakaoPayment,intent,null)

    }


    override fun onResume() {
        super.onResume()
        if(qrCodeReaderView!=null){
        this.qrCodeReaderView!!.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        this.qrCodeReaderView!!.stopCamera()
    }
}