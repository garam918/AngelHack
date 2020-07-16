package com.example.garam.angelhack.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.garam.angelhack.Point.PointQr
import com.example.garam.angelhack.Point.PointUse
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_user_menu.*


class UserMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu)
        val uid = intent.getStringExtra("uid")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        payment.setOnClickListener {
            val qrpay = Intent(this,KakaoPayment::class.java)
            qrpay.putExtra("uid",uid)
            startActivity(qrpay)
        }

        pointUse.setOnClickListener {
            val qrPoint = Intent(this,PointQr::class.java)
            startActivity(qrPoint)
        }
    }
}