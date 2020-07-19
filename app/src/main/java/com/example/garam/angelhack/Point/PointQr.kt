package com.example.garam.angelhack.Point

import android.content.Intent
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.garam.angelhack.R
import com.example.garam.angelhack.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class PointQr : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
    val baseURL = "http://15.165.205.48:8000"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(
        GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit2.create(NetworkService::class.java)
    private var qrCodeReaderView: QRCodeReaderView? = null

    lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_qr)

        val intent = intent
        uid = intent.getStringExtra("uid")
        val qrCodeReaderView = findViewById<QRCodeReaderView>(R.id.qrdecoderview2)
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
        val hid = hostname.get("uid").toString()
        val introduceText = hostname.get("introduceText").toString()
        val hostTextView = findViewById<TextView>(R.id.hostNameInfo2)
        if (hostTextView.text.isNotEmpty()){
            finish()
        }
        else {
            hostTextView.text = hostInfoname
            val intent = Intent(this@PointQr,PointStoreInfo::class.java)
            intent.putExtra("storename",hostInfoname)
            intent.putExtra("introduceText",introduceText)
            intent.putExtra("hid",hid)
            intent.putExtra("uid",uid)
            startActivity(intent)

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