package com.equalinfotech.coffee.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.CoffeedetailsResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_coffee_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoffeeDetailsActivity : BaseActivity(), cont {
    lateinit var hostid: String
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_details)
        tv_backcoffeedetailss.setOnClickListener {
            onBackPressed()
        }
        card_topic.setBackgroundResource(R.drawable.cardsky_background)
        sharprf = shareprefrences(this)
        if (intent.extras != null) {
            hostid = intent.getStringExtra("hostid").toString()
        }
        booking_start.setOnClickListener {
            var intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("hostid", hostid)
            startActivity(intent)
        }
        coffeedetailsdata()

    }


    fun coffeedetailsdata() {
        showProgressDialog()

        var orderdilivery: Call<CoffeedetailsResponse> = APIUtils.getServiceAPI()!!.hostDetail(
            sharprf.getStringPreference(Token).toString(),
            sharprf.getStringPreference(USER_ID).toString(),
            hostid
        )
        orderdilivery.enqueue(object : Callback<CoffeedetailsResponse> {
            override fun onResponse(
                call: Call<CoffeedetailsResponse>,
                response: Response<CoffeedetailsResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            blogtitle.text = response.body()!!.data.hostData.name
                            address.text = response.body()!!.data.hostData.address
                            location.text = response.body()!!.data.hostData.city
                            discrbtion.text = response.body()!!.data.hostData.description
                            if (response!!.body()!!.data.hostOffer.size>0){
                                todayoffer.visibility= View.VISIBLE
                            }else{
                                todayoffer.visibility= View.GONE
                            }

                        } else {
                            showToastMessage(this@CoffeeDetailsActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        startActivity(Intent(this@CoffeeDetailsActivity, LoginActivity::class.java))
                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@CoffeeDetailsActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<CoffeedetailsResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
}