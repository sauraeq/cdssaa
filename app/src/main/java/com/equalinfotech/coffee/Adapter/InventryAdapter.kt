package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.InventoryResponse
import kotlinx.android.synthetic.main.adapter_inventry.view.*

class InventryAdapter(
    var context: Context,
    var inventrylist: ArrayList<InventoryResponse.Data.Inventory>
) : RecyclerView.Adapter<InventryAdapter.InventrHolder>() {
    class InventrHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventrHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_inventry, parent, false)
        return InventrHolder(view)
    }

    override fun onBindViewHolder(holder: InventrHolder, position: Int) {
        if (inventrylist[position].category_name == "Pizza Boxes") {
            holder.itemView.cup.setImageResource(R.drawable.pizzabox)
        } else {
            holder.itemView.cup.setImageResource(R.drawable.pizzabox)
        }
        holder.itemView.returncount.text = inventrylist[position].return_day_left
        holder.itemView.reusable.text =
            inventrylist[position].quantity + " " + inventrylist[position].reusable_items
    }

    override fun getItemCount(): Int {
        return inventrylist.size
    }
}