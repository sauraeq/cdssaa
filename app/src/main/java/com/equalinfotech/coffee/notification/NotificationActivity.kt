package com.equalinfotech.learnorteach.notification

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.learnorteach.notification.notificationadapter.NotificationAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.NotificationResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_notification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : BaseActivity(),cont {
    lateinit var user_id: String
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        tv_backnotificatio.setOnClickListener {
            onBackPressed()
        }
        sharprf=shareprefrences(this)
        membershippurchase()

    }


    fun membershippurchase() {
        showProgressDialog()
        var orderdilivery: Call<NotificationResponse> =
            APIUtils.getServiceAPI()!!.getNotification(
                sharprf.getStringPreference(Token).toString(),
                sharprf.getStringPreference(USER_ID).toString(),
            )
        orderdilivery.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            notificationrecycle.layoutManager = LinearLayoutManager(this@NotificationActivity)
                            notificationrecycle.adapter = NotificationAdapter(this@NotificationActivity,
                                response.body()!!.data)
                            if(response!!.body()!!.data.size>0){
                                txt_notification.visibility=View.GONE
                            }else{
                                txt_notification.visibility=View.VISIBLE
                            }


                        } else {
                            showToastMessage(
                                this@NotificationActivity,
                                response.body()!!.message
                            )
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@NotificationActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
}