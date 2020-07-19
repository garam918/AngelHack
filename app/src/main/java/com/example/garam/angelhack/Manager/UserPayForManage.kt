package com.example.garam.angelhack.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.angelhack.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_user_pay_for_manage.*
import org.json.JSONArray
import org.json.JSONObject

class UserPayForManage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pay_for_manage)
        val json = JSONObject(intent.getStringExtra("json"))
        val sayong = JSONObject(json.toString()).getString("usage_history")
        val before = JSONArray(JSONObject(json.toString()).getString("usage_history")).getString(2)
        val beforemoney = JSONObject(before).getString("before_money")
        Log.e("제이슨", json.toString())
        Log.e("사용기록", JSONObject(json.toString()).getString("usage_history"))
        Log.e("사용기록 2",JSONArray(JSONObject(json.toString()).getString("usage_history")).getString(2))
        Log.e("이전 돈", beforemoney)
        Log.e("크기", before.length.toString())
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        var lists = arrayListOf<payList>()


        userName.text = "고객 : ${JSONObject(json.toString()).getString("name")}"
        for (i in 0 until 3 ){
            var recent = JSONArray(JSONObject(json.toString()).getString("usage_history")).getString(i)
            Log.e("recent",recent)
            var beforemoney = JSONObject(recent).getString("before_money")
            Log.e("before", beforemoney)
            var usedmoney = JSONObject(recent).getString("used_money")
            Log.e("used",usedmoney)
            var useTime = JSONObject(recent).getString("createdAt")
            Log.e("time", useTime)
            lists.add(payList(beforemoney,usedmoney,useTime))
       }

        val test = RecentRecyclerAdapter(lists,this){}
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }
}