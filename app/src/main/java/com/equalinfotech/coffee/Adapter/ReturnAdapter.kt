package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.Activity.OrderReturnActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.ScanDataResponse
import kotlinx.android.synthetic.main.adapter_return.view.*

class ReturnAdapter(var context: Context,var returnlist:ArrayList<ScanDataResponse.Data.ProductReturn>):RecyclerView.Adapter<ReturnAdapter.ReturnHolder>() {
   inner class ReturnHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       init {
           itemView.minius.setOnClickListener {
               if((context as OrderReturnActivity).returndata[position].quantity!=1){
                   addsub(adapterPosition,itemView.quanty,"sub")
               }
           }
           itemView.add.setOnClickListener {
               addsub(adapterPosition,itemView.quanty,"add")

           }
       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_return, parent, false)
        return ReturnHolder(view)
    }

    override fun onBindViewHolder(holder: ReturnHolder, position: Int) {
        if(returnlist[position].category_name=="Pizza Boxes"){
            holder.itemView.imageeee.setImageResource(R.drawable.pizzabox)
        }else{
            holder.itemView.imageeee.setImageResource(R.drawable.cupicon)
        }
        returnlist[position].finalquantity=returnlist[position].quantity
            holder.itemView.nameofproduct.text=returnlist[position].name
            holder.itemView.quanty.text=returnlist[position].quantity.toString()

    }

    override fun getItemCount(): Int {
       return returnlist.size
    }

    fun addsub(position: Int,textView: TextView,type:String){
        if(type=="add"){
            if(returnlist[position].quantity<returnlist[position].finalquantity){
                returnlist[position].quantity=returnlist[position].quantity+1
            }else{
                Toast.makeText(context,"Your Can not apply more than purchases quantity",Toast.LENGTH_SHORT).show()
            }

        }else{
            if(returnlist[position].quantity!=1){
                returnlist[position].quantity=returnlist[position].quantity-1
            }

        }
        (context as OrderReturnActivity).returndata[position].quantity=returnlist[position].quantity
        textView.text==returnlist[position].quantity.toString()
    }
}