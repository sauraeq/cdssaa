package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.Activity.MembershipPlanActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.MembershipResponse
import kotlinx.android.synthetic.main.adapter_membership.view.*


class Membershipadapter(
    var context: Context,
    var memberlist: ArrayList<MembershipResponse.MembForMember>,
    var purchaseshhh: String,
    var membershippurchases:String
) : RecyclerView.Adapter<Membershipadapter.MembershipHolder>() {

    inner class MembershipHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.membershipcard.setOnClickListener {
                if(membershippurchases=="0"){
                    Toast.makeText(context,"You have already purchased the plan .So Please wait for expire",Toast.LENGTH_SHORT).show()
                }else{
                    for (i in 0..memberlist.size - 1) {
                        if (i == position) {
                            memberlist[position].datakey = 1
                        } else {
                            memberlist[i].datakey = 0
                        }
                    }
                    (context as MembershipPlanActivity).membershipid =
                        memberlist[position].membership_id
                    Log.e("after",purchaseshhh)
                    if (memberlist[position].membership_price == "0.00") {
                        checkstatus(purchaseshhh)

                    } else {
                        (context as MembershipPlanActivity).subscriptionType = "paid"
                    }

                    notifyDataSetChanged()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_membership, parent, false)
        return MembershipHolder(view)
    }

    override fun onBindViewHolder(holder: MembershipHolder, position: Int) {
        if (memberlist[position].datakey == 0) {
            val d: Drawable = context.resources
                .getDrawable(R.drawable.greyborder)
            holder.itemView.membershipcard.background = d
//            holder.itemView.discrbtion.setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.purple_200
//                )
//            )

        } else {
            val e: Drawable = context.resources
                .getDrawable(R.drawable.goldenborder)
            holder.itemView.membershipcard.background = e

        }
        holder.itemView.maintile.text = memberlist[position].membership_name
        holder.itemView.txt_price.text = memberlist[position].membership_price
        holder.itemView.txt_peruser.text =
            "Containler to loan per user = " + memberlist[position].containers_to_loan_per_use.toString()
        holder.itemView.txt_total.text =
            "Total containers to loan = " + memberlist[position].total_containers_to_loan.toString()
        holder.itemView.rteurn.text =
            "Days to return container =  " + memberlist[position].days_to_return_containers.toString()

    }

    override fun getItemCount(): Int {
        return memberlist.size
    }


    fun checkstatus(type:String){
        if (type == "No") {
            (context as MembershipPlanActivity).subscriptionType = "paid"
        } else {
            (context as MembershipPlanActivity).subscriptionType = "free"
        }
    }
}