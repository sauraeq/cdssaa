package com.equalinfotech.coffee.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.coffee.Adapter.TermsAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.MembershipSubcrbtionResponse
import com.equalinfotech.coffee.modal.PaymentstatusResponse
import com.equalinfotech.coffee.modal.TermsConditionResponse
import com.equalinfotech.coffee.util.BaseActivity
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_term_condition.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TermConditionActivity : BaseActivity(), cont, View.OnClickListener {
    lateinit var sharprf: shareprefrences
    lateinit var membershipid: String
    lateinit var type: String
    lateinit var ispurchess: String

    lateinit var subscriptionType: String
    var indication: String = "transtaionpage"
    var chekedlist: ArrayList<Int> = arrayListOf()
    var termscontiii: ArrayList<TermsConditionResponse.Tc> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_condition)
        if (indication == "payment") {
            Toast.makeText(this, "comeon", Toast.LENGTH_SHORT).show()
        }
        if (intent.extras != null) {
            membershipid = intent.getStringExtra("membershipid").toString()
            type = intent.getStringExtra("type").toString()
            ispurchess = intent.getStringExtra("ispurchses").toString()
            subscriptionType = intent.getStringExtra("subscriptionType").toString()
        }
        tv_backnotification.setOnClickListener {
            onBackPressed()
        }
        sharprf = shareprefrences(this)
        termsandcondotion()
        continue_term.setOnClickListener(this)

    }

    fun termsandcondotion() {
        showProgressDialog()
        var orderdilivery: Call<TermsConditionResponse> =
            APIUtils.getServiceAPI()!!.getTermsConditionsLists(
                sharprf.getStringPreference(Token).toString(),
            )
        orderdilivery.enqueue(object : Callback<TermsConditionResponse> {
            override fun onResponse(
                call: Call<TermsConditionResponse>,
                response: Response<TermsConditionResponse>
            ) {

                try {
                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            termscontiii =
                                response.body()!!.tc_list as ArrayList<TermsConditionResponse.Tc>
                            termsandcondionrecycle.layoutManager =
                                LinearLayoutManager(this@TermConditionActivity)
                            termsandcondionrecycle.adapter = TermsAdapter(
                                this@TermConditionActivity,
                                response.body()!!.tc_list
                            )

                        } else {
                            showToastMessage(
                                this@TermConditionActivity,
                                response.body()!!.message
                            )
                        }

                    } else if (response.code() == 401) {
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@TermConditionActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<TermsConditionResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun membershippurchase() {
        showProgressDialog()
        var orderdilivery: Call<MembershipSubcrbtionResponse> =
            APIUtils.getServiceAPI()!!.membershippurchase(
                sharprf.getStringPreference(Token).toString(),
                sharprf.getStringPreference(USER_ID).toString(),
                membershipid,
                subscriptionType
            )
        orderdilivery.enqueue(object : Callback<MembershipSubcrbtionResponse> {
            override fun onResponse(
                call: Call<MembershipSubcrbtionResponse>,
                response: Response<MembershipSubcrbtionResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()


                            if (response.body()!!.subscriptionType == "paid") {
                                sharprf.setStringPreference(
                                    "Payemntpayemnt",
                                    response.body()!!.id
                                )
                                openSwishWithToken(
                                    this@TermConditionActivity,
                                    response.body()!!.token,
                                    response.body()!!.callbackUrl
                                )
                            } else {
                                startActivity(
                                    Intent(
                                        this@TermConditionActivity,
                                        DashBoardActivity::class.java
                                    )
                                )
                                finish()
                            }
                            showToastMessage(
                                this@TermConditionActivity,
                                response.body()!!.message
                            )




                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@TermConditionActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<MembershipSubcrbtionResponse>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }


    fun paymentstauts() {
        showProgressDialog()
        var orderdilivery: Call<PaymentstatusResponse> =
            APIUtils.getServiceAPI()!!.getPaymentRequest(
                sharprf.getStringPreference("Payemntpayemnt").toString(),
            )
        orderdilivery.enqueue(object : Callback<PaymentstatusResponse> {
            override fun onResponse(
                call: Call<PaymentstatusResponse>,
                response: Response<PaymentstatusResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        sharprf.setStringPreference("Payemntpayemnt", "")
                        showToastMessage(
                            this@TermConditionActivity,
                            "Payment has been " + response.body()!!.status
                        )
                        startActivity(
                            Intent(
                                this@TermConditionActivity,
                                DashBoardActivity::class.java
                            )
                        )
                        finishAffinity()

                    } else if (response.code() == 401) {
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@TermConditionActivity, "Something went wrong")
                    }
                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<PaymentstatusResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.continue_term -> {
                if (checkboxdata.isChecked) {
                    membershippurchase()
                } else {
                    Toast.makeText(this, "Please accept  conditions", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun openSwishWithToken(context: Context?, token: String?, callBackUrl: String?): Boolean {
        if (token == null || token.length == 0 || callBackUrl == null || callBackUrl.length == 0 || context == null) {
            return false
        }
        // Construct the uri
        // Note that appendQueryParameter takes care of uri encoding
        // the parameters
        val url: Uri = Uri.Builder()
            .scheme("swish")
            .authority("paymentrequest")
            .appendQueryParameter("token", token)
//            .appendQueryParameter("callbackurl", callBackUrl)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, url)
//        intent.setPackage("se.bankgirot.swish.sandbox")
        intent.setPackage("se.bankgirot.swish")
        indication = "payment"

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("errorrr", e.toString())
            // Unable to start Swish return false;
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (sharprf.getStringPreference("Payemntpayemnt")!!.isNotEmpty()) {
            paymentstauts()
        }
    }


}