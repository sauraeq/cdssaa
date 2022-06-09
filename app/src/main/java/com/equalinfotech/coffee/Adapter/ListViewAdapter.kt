package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.coffee.Activity.BookingActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.HomeScreenReponse
import kotlinx.android.synthetic.main.adapter_listview.view.*

class ListViewAdapter(var context: Context, var list: List<HomeScreenReponse.Data.HostArr>) :
    RecyclerView.Adapter<ListViewAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.selectid.setOnClickListener {
                var intent = Intent(context, BookingActivity::class.java)
                intent.putExtra("hostid", list[adapterPosition].hostId.toString())
                intent.putExtra("imageurl", list[adapterPosition].profileImage.toString())
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewAdapter.ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_listview, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewAdapter.ListViewHolder, position: Int) {
        Glide.with(context).load(list[position].profileImage).into(holder.itemView.user_profile)
        holder.itemView.descrion.text = list[position].name
        holder.itemView.list_map.text = list[position].city
        holder.itemView.distenace.text = list[position].distance
    }

    override fun getItemCount(): Int {
        return list.size
    }
}