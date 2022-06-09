package com.equalinfotech.coffee.Activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Adapter.FaqAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.FaqResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.activity_term_condition.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqActivity : BaseActivity(), cont {
    lateinit var sharprf: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        sharprf = shareprefrences(this)
        termsandcondotion()
        searchdatabutton.setOnClickListener {
            if (searchdata.text.toString().trim().isEmpty()) {
                showToastMessage(this, "Please Enter Search")
            } else {
                faqsearch()
            }
        }

        tv_backfaq.setOnClickListener {
            onBackPressed()
        }

    }


    fun termsandcondotion() {
        showProgressDialog()
        var orderdilivery: Call<FaqResponse> = APIUtils.getServiceAPI()!!.getFaqLists(
            sharprf.getStringPreference(Token).toString(),
        )
        orderdilivery.enqueue(object : Callback<FaqResponse> {
            override fun onResponse(
                call: Call<FaqResponse>,
                response: Response<FaqResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            faqrecycle.layoutManager = LinearLayoutManager(this@FaqActivity)
                            faqrecycle.adapter =
                                FaqAdapter(
                                    this@FaqActivity,
                                    response.body()!!.faqs as ArrayList<FaqResponse.Faq>
                                )

                        } else {
                            showToastMessage(this@FaqActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@FaqActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<FaqResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun faqsearch() {
        showProgressDialog()

        var orderdilivery: Call<FaqResponse> = APIUtils.getServiceAPI()!!.faqSearchList(
            sharprf.getStringPreference(Token).toString(),
            searchdata.text.toString(),
        )
        orderdilivery.enqueue(object : Callback<FaqResponse> {
            override fun onResponse(
                call: Call<FaqResponse>,
                response: Response<FaqResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            faqrecycle.layoutManager = LinearLayoutManager(this@FaqActivity)
                            faqrecycle.adapter =
                                FaqAdapter(
                                    this@FaqActivity,
                                    response.body()!!.faqs as ArrayList<FaqResponse.Faq>
                                )

                            if (response.body()!!.faqs.size > 0) {
                                errormessage.visibility = View.GONE
                            } else {
                                errormessage.visibility = View.VISIBLE
                            }

                        } else {
                            showToastMessage(this@FaqActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@FaqActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<FaqResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
}