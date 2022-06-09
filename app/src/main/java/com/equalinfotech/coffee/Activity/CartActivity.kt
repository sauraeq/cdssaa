package com.equalinfotech.coffee.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Adapter.CartAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.CartResponse
import com.equalinfotech.coffee.modal.Checkoutresponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : BaseActivity(), cont {
    lateinit var srph: shareprefrences
    lateinit var cartdata: ArrayList<CartResponse.CartProduct>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        srph = shareprefrences(this)
        tv_backcart.setOnClickListener {
            onBackPressed()
        }
        cartdata = arrayListOf()
        cartlist()
        text_continue.setOnClickListener {
            if (cartdata.size == 0) {
                showToastMessage(this@CartActivity, "First Select Product")
            } else {
                orderplace()
            }
        }
    }


    fun orderplace() {
        showProgressDialog()
        var data: String = ""
        for (i in 0..cartdata.size - 1) {
            if (i == cartdata.size - 1) {
                data = data + cartdata[i].cart_id
            } else {
                data = data + cartdata[i].cart_id + ","
            }
        }
        var orderdilivery: Call<Checkoutresponse> = APIUtils.getServiceAPI()!!.checkout(
            srph.getStringPreference(Token).toString(),
            srph.getStringPreference(USER_ID).toString(),
            data
        )
        orderdilivery.enqueue(object : Callback<Checkoutresponse> {
            override fun onResponse(
                call: Call<Checkoutresponse>,
                response: Response<Checkoutresponse>
            ) {
                try {
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            startActivity(
                                Intent(
                                    this@CartActivity,
                                    OrderSucessfullActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            var inetntdata=Intent(this@CartActivity,MembershipPlanActivity::class.java)
                            inetntdata.putExtra("type","cart")
                            startActivity(inetntdata)
                            showToastMessage(this@CartActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {

                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@CartActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {
                    Log.e("chgfg", e.toString())

                }

            }

            override fun onFailure(call: Call<Checkoutresponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun cartlist() {
        showProgressDialog()
        var orderdilivery: Call<CartResponse> = APIUtils.getServiceAPI()!!.getCartItems(
            srph.getStringPreference(USER_ID).toString(),
            srph.getStringPreference(Token).toString()

        )
        orderdilivery.enqueue(object : Callback<CartResponse> {
            override fun onResponse(
                call: Call<CartResponse>,
                response: Response<CartResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            showToastMessage(this@CartActivity, response.body()!!.message)
                            cartdata =
                                response.body()!!.cartProductList as ArrayList<CartResponse.CartProduct>
                            text_subtotal.text = "$ " + response.body()!!.sub_total.toString()
                            text_total.text = "$ " + response.body()!!.total.toString()
                            txt_offer.text = response.body()!!.offer.toString()
                            cartrecycle.layoutManager = LinearLayoutManager(this@CartActivity)
                            cartrecycle.adapter = CartAdapter(
                                this@CartActivity,
                                response.body()!!.cartProductList as ArrayList<CartResponse.CartProduct>
                            )
                        } else {
                            showToastMessage(this@CartActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@CartActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                Log.e("vcgfhgsddv", t.toString())
                hideProgressDialog()
            }

        })
    }
}