package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.FaqResponse
import kotlinx.android.synthetic.main.faq_adapter.view.*

class FaqAdapter(var context: Context, var list: ArrayList<FaqResponse.Faq>) :
    RecyclerView.Adapter<FaqAdapter.FaqHolder>() {
    class FaqHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.image.setOnClickListener {
                if (itemView.textdescrbtion.visibility == View.VISIBLE) {
                    itemView.image.setImageResource(R.drawable.bluedown)
                    itemView.textdescrbtion.visibility = View.GONE
                } else {
                    itemView.image.setImageResource(R.drawable.blueup)
                    itemView.textdescrbtion.visibility = View.VISIBLE
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.faq_adapter, parent, false)
        return FaqAdapter.FaqHolder(view)
    }

    override fun onBindViewHolder(holder: FaqHolder, position: Int) {
        holder.itemView.tesss.text = list[position].faq_question
        holder.itemView.textdescrbtion.text = list[position].faq_answer
    }

    override fun getItemCount(): Int {
        return list.size
    }
}