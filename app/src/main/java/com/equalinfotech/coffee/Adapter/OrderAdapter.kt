package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.coffee.Activity.BookingActivity
import com.equalinfotech.coffee.Activity.OrderReturnActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.BookingResponse
import com.equalinfotech.coffee.modal.ScanDataResponse
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import kotlinx.android.synthetic.main.adapter_booking.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderAdapter(
    var context: Context,
    var list: ArrayList<ScanDataResponse.Data.HostProduct>,
    var hostid: String
) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {
    lateinit var sharprf: shareprefrences

    inner class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.txt_bookproduct.setOnClickListener {
                if (itemView.txt_bookproduct.text != "booked >>") {
                    bookingproduct(adapterPosition, itemView.txt_bookproduct)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_booking, parent, false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        sharprf = shareprefrences(context)
        Glide.with(context).load(list[position].image).into(holder.itemView.blogimAGE)
        holder.itemView.txt_tittle.text = list[position].name
        holder.itemView.txt_Describtion.text = list[position].description
        holder.itemView.price.text = "USD " + list[position].price
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun bookingproduct(position: Int, textView: TextView) {
        (context as OrderReturnActivity).showProgressDialog()

        var orderdilivery: Call<BookingResponse> =
            APIUtils.getServiceAPI()!!.bookProduct(
                sharprf.getStringPreference((context as OrderReturnActivity).Token).toString(),
                sharprf.getStringPreference((context as OrderReturnActivity).USER_ID).toString(),
                list[position].pId,
                hostid,
                list[position].price
            )
        orderdilivery.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        (context as OrderReturnActivity).hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            textView.text = "booked >>"
                            (context as BookingActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                            (context as OrderReturnActivity).cartqun="1"

                        } else {
                            (context as OrderReturnActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                        }
                    } else if (response.code() == 401) {
                        (context as OrderReturnActivity).finishAffinity()
                    } else if (response.code() == 400) {
                        (context as OrderReturnActivity).hideProgressDialog()
                        (context as OrderReturnActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )
                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                (context as OrderReturnActivity).hideProgressDialog()
            }
        })
    }

}