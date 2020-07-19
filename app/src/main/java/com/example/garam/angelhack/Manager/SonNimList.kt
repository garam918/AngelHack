package com.example.garam.angelhack.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.angelhack.R

class SonNimList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_son_nim_list)

        val intent = intent
        val name = intent.getStringExtra("name")
        val money = intent.getStringExtra("money")
        val recycler = findViewById<RecyclerView>(R.id.sonRecycler)
        var lists = arrayListOf<userList>()
        lists.add(userList(name,money))

        val test = SonNimRecyclerAdapter(lists,this){}
        recycler.adapter = test
        test.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }
}