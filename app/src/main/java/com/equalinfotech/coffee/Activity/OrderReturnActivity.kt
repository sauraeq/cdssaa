package com.equalinfotech.coffee.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.equalinfotech.coffee.Adapter.ViewpagerAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.fragment.OrderFragment
import com.equalinfotech.coffee.fragment.RetrunFragment
import com.equalinfotech.coffee.modal.ScanDataResponse
import com.equalinfotech.coffee.util.BaseActivity
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_home_details.viewpager
import kotlinx.android.synthetic.main.activity_order_return.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderReturnActivity : BaseActivity(), cont {
    var data: String = ""
    lateinit var cartqun: String
    var hostid: String = ""
    lateinit var shrpre: shareprefrences
    lateinit var returndata: ArrayList<ScanDataResponse.Data.ProductReturn>
    lateinit var bookingitem: ArrayList<ScanDataResponse.Data.HostProduct>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_return)
        tv_order_back.setOnClickListener {
            onBackPressed()
        }
        initclick()
        returndata = arrayListOf()
        bookingitem = arrayListOf()
        shrpre = shareprefrences(this)
        if (intent.extras != null) {
            data = intent.getStringExtra("scandata").toString()
        }
        scandata()

    }

    private fun initclick() {
        tab_order.setOnClickListener {
            callfragment(OrderFragment())
            setbackgroundblue(tab_order)
            setbackgroundnormal(tab_return)
        }
        tab_return.setOnClickListener {
            callfragment(RetrunFragment())
            setbackgroundblue(tab_return)
            setbackgroundnormal(tab_order)
        }
    }


    fun callfragment(fragment: Fragment) {
        val adapter = ViewpagerAdapter(supportFragmentManager)
        adapter.addFragment(fragment, "Search")
        viewpager.adapter = adapter
    }

    fun setbackgroundblue(textView: TextView) {
        textView.setBackgroundResource(R.drawable.colorfill)
        textView.setTextColor(resources.getColor(R.color.white))
        textView.setPadding(0, 8, 0, 8)
    }

    fun setbackgroundnormal(textView: TextView) {
        textView.setBackgroundResource(R.drawable.border)
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setPadding(0, 8, 0, 8)
    }

    fun scandata() {
        showProgressDialog()
        var orderdilivery: Call<ScanDataResponse> = APIUtils.getServiceAPI()!!.scanqrcode(
            shrpre.getStringPreference(Token).toString(),
            shrpre.getStringPreference(USER_ID).toString(),
            data
        )
        orderdilivery.enqueue(object : Callback<ScanDataResponse> {
            override fun onResponse(
                call: Call<ScanDataResponse>,
                response: Response<ScanDataResponse>
            ) {
                try {
                    returndata.clear()
                    bookingitem.clear()
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            hostid = response.body()!!.data.hostId
                            bookingitem =
                                response.body()!!.data.hostProduct as ArrayList<ScanDataResponse.Data.HostProduct>
                            returndata =
                                response.body()!!.data.productReturnList as ArrayList<ScanDataResponse.Data.ProductReturn>
                            callfragment(OrderFragment())
                            setbackgroundblue(tab_order)
                        } else {

                        }
                    } else if (response.code() == 401) {
                        startActivity(Intent(this@OrderReturnActivity, LoginActivity::class.java))
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                    }
                } catch (e: Exception) {
                    Log.e("exceptiooooo", e.toString())
                }
            }

            override fun onFailure(call: Call<ScanDataResponse>, t: Throwable) {
                Log.e("failure", t.toString())
                hideProgressDialog()
            }

        })
    }
}