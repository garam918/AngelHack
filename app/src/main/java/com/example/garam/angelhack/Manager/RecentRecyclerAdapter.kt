package com.example.garam.angelhack.Manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.angelhack.R


class RecentRecyclerAdapter (
    private val items: ArrayList<payList>,
    val context: Context, val itemClick: (payList)-> Unit ) : RecyclerView.Adapter<RecentRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentRecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.userlist,parent,false),itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bind(items[position])
    }

    inner class ViewHolder (itemView: View, itemClick: (payList)-> Unit) : RecyclerView.ViewHolder(itemView) {

        private val beforeMoney = itemView?.findViewById<TextView>(R.id.berfore_money)
        private val usedMoney = itemView?.findViewById<TextView>(R.id.used_money)
        private val usingTime = itemView?.findViewById<TextView>(R.id.usingTime)

        fun bind (list: payList){
            beforeMoney.text = "${list.before_money}원"
            usedMoney.text = "${list.usemoney}원"
            usingTime.text = list.usingTime
            itemView.setOnClickListener { itemClick(list) }
        }
    }
}