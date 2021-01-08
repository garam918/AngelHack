package com.example.garam.angelhack.User

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        val intent = intent
        val money = intent.getStringExtra("money")
        val name = intent.getStringExtra("name")
        val storename = intent.getStringExtra("storename")
        userNameInfo.text = name

        myPointInfo.setOnClickListener {
            val intent = Intent(this,PointList::class.java)
            intent.putExtra("name",name)
            intent.putExtra("storename",storename)
            intent.putExtra("money",money)
            startActivity(intent)
        }
    }
}