package com.equalinfotech.coffee.Activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Adapter.OderHistoryAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.OrderHistoryResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_order_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryActivity : BaseActivity(), cont, View.OnClickListener {
    var month_name: String = ""
    var currentmonth: String = ""
    var year_name: String = ""
    var count: Int = 0
    val cal: Calendar = Calendar.getInstance()
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        tv_orderhistory.setOnClickListener {
            onBackPressed()
        }
        sharprf = shareprefrences(this)
        card_topic.setBackgroundResource(R.drawable.cardsky_background)

        getmonthyear("nothing")
        decreasemonth.setOnClickListener {
            getmonthyear("sub")
        }
        increasesdate.setOnClickListener(this)

    }


    fun getmonthyear(data: String) {

        if (data == "add") {
            cal.add(Calendar.MONTH, 1)
            if (month_name == "December") {
                cal.add(Calendar.YEAR, 1)
            }
        } else if (data == "sub") {
            cal.add(Calendar.MONTH, -1)
            if (month_name == "January") {
                cal.add(Calendar.YEAR, -1)
            }
        }

        val month_date = SimpleDateFormat("MMMM")
        month_name = month_date.format(cal.time)
        val Year_date = SimpleDateFormat("yyyy")
        val year_name = Year_date.format(cal.time)
        if (count == 0) {
            currentmonth = month_name
            count++
        }
        texttt.text = month_name + " " + year_name
        orderhistory(month_name + " " + year_name)
    }


    fun orderhistory(month: String) {
        showProgressDialog()
        var orderdilivery: Call<OrderHistoryResponse> =
            APIUtils.getServiceAPI()!!.orderHistory(
                sharprf.getStringPreference(Token).toString(),
                sharprf.getStringPreference(USER_ID).toString(),
                month
            )
        orderdilivery.enqueue(object : Callback<OrderHistoryResponse> {
            override fun onResponse(
                call: Call<OrderHistoryResponse>,
                response: Response<OrderHistoryResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            orderhistoryrecycle.layoutManager =
                                LinearLayoutManager(this@OrderHistoryActivity)
                            orderhistoryrecycle.adapter = OderHistoryAdapter(
                                this@OrderHistoryActivity,
                                response.body()!!.data.order_data as ArrayList<OrderHistoryResponse.Data.OrderData>

                            )
                            coffeecount.text = response.body()!!.data.coffee_ord_count.toString()
                            pizzacount.text = response.body()!!.data.pizza_ord_count.toString()
                            if (response.body()!!.data.order_data.size > 0) {
                                errororder.visibility = View.GONE
                            } else {
                                errororder.visibility = View.VISIBLE
                            }

                        } else {
                            showToastMessage(
                                this@OrderHistoryActivity,
                                response.body()!!.message
                            )
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@OrderHistoryActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.increasesdate -> {
                if (currentmonth != month_name) {
                    getmonthyear("add")
                }
            }
        }
    }
}