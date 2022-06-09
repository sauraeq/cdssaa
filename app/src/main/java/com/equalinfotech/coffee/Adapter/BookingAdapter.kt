package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.BookingActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.BookingResponse
import com.equalinfotech.coffee.modal.ProductListResponse
import kotlinx.android.synthetic.main.adapter_booking.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingAdapter(
    var context: Context,
    var list: List<ProductListResponse.Data.HostProduct>,
    var hostid: String
) : RecyclerView.Adapter<BookingAdapter.BookingHolder>() {
    lateinit var sharprf: shareprefrences

    inner class BookingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

            itemView.txt_bookproduct.setOnClickListener {
                if (itemView.txt_bookproduct.text != "booked >>") {
                    bookingproduct(adapterPosition, itemView.txt_bookproduct)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingAdapter.BookingHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_booking, parent, false)
        return BookingHolder(view)
    }

    override fun onBindViewHolder(holder: BookingAdapter.BookingHolder, position: Int) {
        sharprf = shareprefrences(context)
        holder.itemView.txt_bookproduct.visibility = View.GONE
        Glide.with(context).load(list[position].image).into(holder.itemView.blogimAGE)
        holder.itemView.txt_tittle.text = list[position].name
        holder.itemView.txt_Describtion.text = list[position].description
        holder.itemView.price.text = "USD " + list[position].price
        holder.itemView.price.text = "USD " + list[position].price
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun bookingproduct(position: Int, textView: TextView) {
        (context as BookingActivity).showProgressDialog()

        var orderdilivery: Call<BookingResponse> =
            APIUtils.getServiceAPI()!!.bookProduct(
                sharprf.getStringPreference((context as BookingActivity).Token).toString(),
                sharprf.getStringPreference((context as BookingActivity).USER_ID).toString(),
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
                        (context as BookingActivity).hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            textView.text = "booked >>"
                            (context as BookingActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )

                        } else {
                            (context as BookingActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                        }
                    } else if (response.code() == 401) {
                        (context as BookingActivity).finishAffinity()
                    } else if (response.code() == 400) {
                        (context as BookingActivity).hideProgressDialog()
                        (context as BookingActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )
                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                (context as BookingActivity).hideProgressDialog()
            }
        })
    }
}