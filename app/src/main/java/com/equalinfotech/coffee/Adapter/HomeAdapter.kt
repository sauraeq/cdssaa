package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.coffee.Activity.HomeDetailsActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.HomeScreenReponse
import kotlinx.android.synthetic.main.adapter_home.view.*

class HomeAdapter(var context: Context, var list: List<HomeScreenReponse.Data.HostArr>) :
    RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.coololist.setOnClickListener {
                context.startActivity(Intent(context, HomeDetailsActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_home, parent, false)
        return HomeHolder(view)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        if(list[position].profileImage!=null){
            Glide.with(context).load(list[position].profileImage).into(holder.itemView.data_image)
        }
        holder.itemView.coffee_name.text = list[position].name
        holder.itemView.location.text = list[position].city
    }

    override fun getItemCount(): Int {
        return list.size
    }
}