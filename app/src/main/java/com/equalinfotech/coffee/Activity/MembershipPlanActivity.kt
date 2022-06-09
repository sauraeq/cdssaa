package com.equalinfotech.coffee.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.coffee.Adapter.Membershipadapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.MembershipResponse
import com.equalinfotech.coffee.util.BaseActivity
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_membership_plan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MembershipPlanActivity : BaseActivity(), cont {
    var membershipid: String = ""
    var type: String = "member"
    var subscriptionType: String = "free"
    var ispurchses: String = "free"
    lateinit var shrpre: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership_plan)
        if (intent.extras != null) {
            type = intent.getStringExtra("type").toString()
        }
        shrpre = shareprefrences(this)
        membership()
        txt_memberback.setOnClickListener {
            onBackPressed()
        }

        txt_continue.setOnClickListener {
            if (membershipid.isEmpty()) {
                Toast.makeText(this, "First Select Plan", Toast.LENGTH_SHORT).show()
            }
//            else if (!isSwishInstalled(this)) {
//                Toast.makeText(
//                    this,
//                    "Please First install swish app then go to payment",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            else {
                var intent = Intent(this, TermConditionActivity::class.java)
                intent.putExtra("membershipid", membershipid)
                intent.putExtra("type", type)
                intent.putExtra("subscriptionType", subscriptionType)
                intent.putExtra("purchases", ispurchses)
                startActivity(intent)
            }
        }
        memberplanrecycle.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

//        txt_continue.setOnClickListener {
//            startActivity(Intent(this,PaymentActivity::class.java))
//        }
//        memberplanrecycle.adapter=Membershipadapter(this)
        memneryetrecycle.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
//        memneryetrecycle.adapter=Membershipadapter(this)

    }


    fun membership() {
        var orderdilivery: Call<MembershipResponse> = APIUtils.getServiceAPI()!!.getMembershipLists(
            shrpre.getStringPreference(Token).toString(),
            shrpre.getStringPreference(USER_ID).toString()
        )
        orderdilivery.enqueue(object : Callback<MembershipResponse> {
            override fun onResponse(
                call: Call<MembershipResponse>,
                response: Response<MembershipResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            memberplanrecycle.layoutManager = LinearLayoutManager(
                                this@MembershipPlanActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            ispurchses = response!!.body()!!.memb_purchase_status
                            response.body()?.memb_purchase_status?.let { Log.e("printbefore", it) }
                            memberplanrecycle.adapter = Membershipadapter(
                                this@MembershipPlanActivity,
                                response.body()!!.memb_list_for_member as ArrayList<MembershipResponse.MembForMember>,
                                response!!.body()!!.memb_purchase_status,
                                response!!.body()!!.allow_purchase,
                            )
                        } else {

                        }
                    } else if (response.code() == 401) {
                        startActivity(
                            Intent(
                                this@MembershipPlanActivity,
                                LoginActivity::class.java
                            )
                        )
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                    }
                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<MembershipResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

//    fun isSwishInstalled(context: Context): Boolean {
//
//      return try {
//            context.packageManager
//                .getPackageInfo("se.bankgirot.swish", 0)
//            true
//        } catch (e: PackageManager.NameNotFoundException) {
//            // Swish app is not installed
//            false
//        }
//    }
}