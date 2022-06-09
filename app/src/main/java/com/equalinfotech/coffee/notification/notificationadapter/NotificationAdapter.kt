package com.equalinfotech.learnorteach.notification.notificationadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.NotificationResponse
import kotlinx.android.synthetic.main.adpter_notification.view.*

class NotificationAdapter(
    var context: Context,
    var notificationlist: List<NotificationResponse.Data>
):RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {
    class NotificationHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            LayoutInflater.from(context).inflate(
                R.layout.adpter_notification,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
holder.itemView.notifcationname.text=notificationlist[position].title
holder.itemView.category.text=notificationlist[position].message
holder.itemView.time.text=notificationlist[position].createdAt

    }

    override fun getItemCount(): Int {
        return  notificationlist.size
    }
}