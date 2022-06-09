package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.OrderHistoryResponse
import kotlinx.android.synthetic.main.adapter_orderhistory.view.*

class OderHistoryAdapter(var context: Context,var orderlist:ArrayList<OrderHistoryResponse.Data.OrderData>) :
    RecyclerView.Adapter<OderHistoryAdapter.OderHistoryHolder>() {
    class OderHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OderHistoryHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_orderhistory, parent, false)
        return OderHistoryHolder(view)
    }

    override fun onBindViewHolder(holder: OderHistoryHolder, position: Int) {
        Glide.with(context).load(orderlist[position].image).into(holder.itemView.blogimAGE)
        holder.itemView.txt_tittle.text=orderlist[position].name
        holder.itemView.txt_Describtion.text=orderlist[position].host_name
        holder.itemView.datetime.text=orderlist[position].ordered_date
    }

    override fun getItemCount(): Int {
        return orderlist.size
    }
}