package com.example.garam.angelhack.Point

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garam.angelhack.R
import com.example.garam.angelhack.User.UserMenu
import kotlinx.android.synthetic.main.activity_point_use_fininsh.*

class PointUseFininsh : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_use_fininsh)

        val intent = intent
        val uid = intent.getStringExtra("uid")
        val remain = intent.getIntExtra("remain_money",0)
        val name = intent.getStringExtra("name")
        nameunPoint.text = "잔여 POINT : ${remain}원"

        homeButton3.setOnClickListener {
            val intent = Intent(this, UserMenu::class.java)
            intent.putExtra("uid",uid)
            intent.putExtra("name",name)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}