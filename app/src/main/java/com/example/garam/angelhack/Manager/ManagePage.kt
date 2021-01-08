package com.example.garam.angelhack.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_manage_page.*

class ManagePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_page)

        val intent = intent
        val hid = intent.getStringExtra("hid")

        qrButton.setOnClickListener {
            Glide.with(this).load("http://15.165.205.48:8000/uploads/$hid.jpg").error(R.drawable.ic_launcher_background).skipMemoryCache(true).diskCacheStrategy(
                DiskCacheStrategy.NONE).into(qrImageView)

        }

    }
}