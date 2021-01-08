package com.example.garam.angelhack.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_point_list.*

class PointList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_list)

        val intent = intent
        val money = intent.getStringExtra("money")
        val name = intent.getStringExtra("storename")

        storeList1.text = "$name : ${money}원"
    }
}