package com.example.garam.angelhack.Point

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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class PointQr : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
    val baseURL = "https://80fac4eb1b11.ngrok.io"
    val retrofit2: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(
        GsonConverterFactory.create()).client(
        OkHttpClient.Builder().connectTimeout(1,
            TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).addInterceptor(
            HttpLoggingInterceptor()
        ).build()).build()
    val networkService = retrofit2.create(NetworkService::class.java)
    private var qrCodeReaderView: QRCodeReaderView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_qr)

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
        val hostname = JsonParser().parse(text) as JsonObject
        val hostInfoname = hostname.get("storename").toString()
        val hostTextView = findViewById<TextView>(R.id.hostNameInfo)
        if (hostTextView.text.isNotEmpty()){
            finish()
        }
        else {

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