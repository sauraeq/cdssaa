package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.Activity.TermConditionActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.TermsConditionResponse
import kotlinx.android.synthetic.main.adapter_terms.view.*

class TermsAdapter(var context: Context, var termlist: List<TermsConditionResponse.Tc>) :
    RecyclerView.Adapter<TermsAdapter.TermsHolder>() {
    class TermsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_terms, parent, false)
        return TermsHolder(view)
    }

    override fun onBindViewHolder(holder: TermsHolder, position: Int) {
        var list = termlist[position]
        holder.itemView.contains.text = termlist[position].tc_content
        holder.itemView.checkbox.setOnClickListener {
            if (holder.itemView.checkbox.isChecked) {
//                Toast.makeText(context, "Chceked", Toast.LENGTH_SHORT).show()
                if (!(context as TermConditionActivity).chekedlist.contains(list.tc_id.toInt())) {
                    (context as TermConditionActivity).chekedlist.add(list.tc_id.toInt())
                    Log.e("finallistadd", (context as TermConditionActivity).chekedlist.toString())

                }
            } else {
                if ((context as TermConditionActivity).chekedlist.contains(list.tc_id.toInt())) {
                    (context as TermConditionActivity).chekedlist.remove(list.tc_id.toInt())
                    Log.e("finallistremove", (context as TermConditionActivity).chekedlist.toString())

                }
//                Toast.makeText(context, "unchecked", Toast.LENGTH_SHORT).show()
            }
            Log.e("finallist", (context as TermConditionActivity).chekedlist.toString())

        }

    }

    override fun getItemCount(): Int {
        return termlist.size
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position
}