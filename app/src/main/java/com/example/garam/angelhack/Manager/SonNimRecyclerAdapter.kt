package com.example.garam.angelhack.Manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.angelhack.R

class SonNimRecyclerAdapter (
    val items: ArrayList<userList>,
    val context: Context, val itemClick: (userList)-> Unit ) : RecyclerView.Adapter<SonNimRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SonNimRecyclerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.sonnimlist, parent, false),
            itemClick
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick: (userList) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        // Holds the TextView that will add each animal to
        val userName = itemView?.findViewById<TextView>(R.id.sonNim)
        val nameun = itemView?.findViewById<TextView>(R.id.nameunDon)

        fun bind(list: userList) {
            userName.text = "${list.userName}원"
            nameun.text = "${list.remain_money}원"
//            itemView.setOnClickListener { itemClick(list) }
        }
    }
}