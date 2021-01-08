package com.example.garam.angelhack.Manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.angelhack.R
import kotlinx.android.synthetic.main.activity_user_pay_for_manage.*
import org.json.JSONArray
import org.json.JSONObject

class UserPayForManage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pay_for_manage)
        val json = JSONObject(intent.getStringExtra("json"))
        val before = JSONArray(JSONObject(json.toString()).getString("usage_history")).getString(2)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        val lists = arrayListOf<payList>()

        userName.text = "고객 : ${JSONObject(json.toString()).getString("name")}"
        for (i in 0 until 3 ){
            val recent = JSONArray(JSONObject(json.toString()).getString("usage_history")).getString(i)
            val beforemoney = JSONObject(recent).getString("before_money")
            val usedmoney = JSONObject(recent).getString("used_money")
            val useTime = JSONObject(recent).getString("createdAt")
            lists.add(payList(beforemoney,usedmoney,useTime))
       }

        val test = RecentRecyclerAdapter(lists,this){}
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }
}