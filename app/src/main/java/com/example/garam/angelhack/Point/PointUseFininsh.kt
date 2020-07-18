package com.example.garam.angelhack.Point

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_point_use_fininsh.*

class PointUseFininsh : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_use_fininsh)

        val intent = intent
        val remain = intent.getIntExtra("remain_money",0)

        nameunPoint.text = "잔여 POINT : ${remain}원"
    }
}