package com.example.garam.angelhack.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.angelhack.R

class UserPayForManage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pay_for_manage)

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        var lists = arrayListOf<payList>()

        val test = RecentRecyclerAdapter(lists,this){

            payList ->
        }
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }
}