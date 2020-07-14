package com.example.garam.angelhack.User

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.garam.angelhack.R


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

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    override fun onQRCodeRead(text: String?, points: Array<PointF?>?) {
        Log.e("텍스트","$text")
        val resultText = findViewById<TextView>(R.id.resultQR)
        resultText.text = text
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