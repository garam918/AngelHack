package com.example.garam.angelhack.Point

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_point_store_info.*

class PointStoreInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_store_info)
        val intnet = intent
        val storename = intnet.getStringExtra("storename")
        val introduce = intent.getStringExtra("introduceText")
        val uid = intnet.getStringExtra("uid")
        val hid = intent.getStringExtra("hid")
        pointStoreName.text = storename
        pointStoreInfo.text = introduce

        pointPay.setOnClickListener {
            val intent = Intent(this,PointUse::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("hid",hid)
            startActivity(intent)
        }
    }
}