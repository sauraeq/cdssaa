package com.equalinfotech.coffee.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.CartActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.AddCartResponse
import com.equalinfotech.coffee.modal.CartDeleteResponse
import com.equalinfotech.coffee.modal.CartResponse
import kotlinx.android.synthetic.main.adapter_card.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(var context: Context, var cartlist: ArrayList<CartResponse.CartProduct>) :
    RecyclerView.Adapter<CartAdapter.CartHolder>() {
    lateinit var sharprf: shareprefrences
    var count: Int = 1

    inner class CartHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.delitem.setOnClickListener {
                carddel(adapterPosition)
            }
            itemView.additem.setOnClickListener {
                addcard(adapterPosition, "increment", itemView.txt_quanty)
            }
            itemView.removeitem.setOnClickListener {
                calldata(adapterPosition, itemView.txt_quanty)
            }
        }
    }

    private fun calldata(position: Int, text: TextView) {
        if (cartlist[position].quantity != 1) {
            addcard(position, "decrement", text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_card, parent, false)
        return CartHolder(view)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        sharprf = shareprefrences(context)
        holder.itemView.txt_tittle.text = cartlist[position].name
        holder.itemView.txt_quanty.text = cartlist[position].quantity.toString()
        holder.itemView.txt_Describtion.text = cartlist[position].description
        holder.itemView.product_price.text = "$ " + cartlist[position].price
        Glide.with(context).load(cartlist[position].image).into(holder.itemView.cardimage)
    }

    override fun getItemCount(): Int {
        return cartlist.size
    }

    fun carddel(position: Int) {
        (context as CartActivity).showProgressDialog()
        var orderdilivery: Call<CartDeleteResponse> =
            APIUtils.getServiceAPI()!!.removeCartById(
                sharprf.getStringPreference((context as CartActivity).Token).toString(),
                sharprf.getStringPreference((context as CartActivity).USER_ID).toString(),
                cartlist[position].cart_id

            )
        orderdilivery.enqueue(object : Callback<CartDeleteResponse> {
            override fun onResponse(
                call: Call<CartDeleteResponse>,
                response: Response<CartDeleteResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        (context as CartActivity).hideProgressDialog()
                        if (response.body()!!.status == "success") {
//                            cartlist.removeAt(position)
//                            notifyDataSetChanged()
                            (context as CartActivity).cartlist()
                            (context as CartActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                        } else {
                            (context as CartActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                        }
                    } else if (response.code() == 401) {
                        (context as CartActivity).finishAffinity()
                    } else if (response.code() == 400) {
                        (context as CartActivity).hideProgressDialog()
                        (context as CartActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )
                    }

                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<CartDeleteResponse>, t: Throwable) {
                (context as CartActivity).hideProgressDialog()
            }
        })
    }

    fun addcard(position: Int, type: String, text: TextView) {
        (context as CartActivity).showProgressDialog()

        var orderdilivery: Call<AddCartResponse> =
            APIUtils.getServiceAPI()!!.addRemoveQuantityCart(
                sharprf.getStringPreference((context as CartActivity).Token).toString(),
                sharprf.getStringPreference((context as CartActivity).USER_ID).toString(),
                cartlist[position].cart_id,
                type,
            )
        orderdilivery.enqueue(object : Callback<AddCartResponse> {
            override fun onResponse(
                call: Call<AddCartResponse>,
                response: Response<AddCartResponse>
            ) {

                try {
                    if (response.code() == 200) {
                        (context as CartActivity).hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            (context as CartActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                            if (type == "increment") {
                                cartlist[position].quantity= cartlist[position].quantity++
                            } else {
                                cartlist[position].quantity= cartlist[position].quantity--
                            }
                            text.text = cartlist[position].quantity.toString()
                            (context as CartActivity).cartlist()

                        } else {
                            (context as CartActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )
                        }

                    } else if (response.code() == 401) {
                        (context as CartActivity).finishAffinity()
                    } else if (response.code() == 400) {
                        (context as CartActivity).hideProgressDialog()
                        (context as CartActivity).showToastMessage(
                            context,
                            response.body()!!.message
                        )
                    }

                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                (context as CartActivity).hideProgressDialog()
            }
        })
    }
}