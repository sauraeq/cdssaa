package com.equalinfotech.coffee.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Adapter.BookingAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.ProductListResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_coffee_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingActivity : BaseActivity(), cont {
    lateinit var hostid: String
    lateinit var imageurl: String
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        sharprf = shareprefrences(this)
        if (intent.extras != null) {
            hostid = intent.getStringExtra("hostid").toString()
            imageurl = intent.getStringExtra("imageurl").toString()
        }
        Glide.with(this).load(imageurl).into(bloffgimage)
        tv_backbutton.setOnClickListener {
            onBackPressed()
        }
        productlist()
        txt_gotocart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

//        coffereteurm.setBackgroundResource(R.drawable.cardsky_background)
    }

    fun productlist() {
        showProgressDialog()
        var orderdilivery: Call<ProductListResponse> = APIUtils.getServiceAPI()!!.productLists(
            sharprf.getStringPreference(Token).toString(),
            sharprf.getStringPreference(USER_ID).toString(),
            hostid
        )
        orderdilivery.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            coffereteurm.layoutManager = LinearLayoutManager(this@BookingActivity)
                            coffereteurm.adapter = BookingAdapter(
                                this@BookingActivity,
                                response.body()!!.data.hostProduct, hostid
                            )
                            if(response!!.body()!!.data.hostProduct.size>0){
                                showerror.visibility=View.GONE
                            }else{
                                showerror.visibility=View.VISIBLE
                            }
                        } else {
                            showToastMessage(this@BookingActivity, response.body()!!.message)
                        }
                    } else if (response.code() == 401) {
                        startActivity(Intent(this@BookingActivity, LoginActivity::class.java))
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@BookingActivity, response.body()!!.message)
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

}