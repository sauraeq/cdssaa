package com.equalinfotech.coffee.Activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.PrivacyResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_privacy.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrivacyActivity : BaseActivity(),cont {
    lateinit var shrphr:shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        shrphr= shareprefrences(this)
        privacy()
        tv_backnotificatio.setOnClickListener {
            onBackPressed()
        }
    }

    fun privacy() {
        showProgressDialog()
        var orderdilivery: Call<PrivacyResponse> = APIUtils.getServiceAPI()!!.cms_content(
            shrphr.getStringPreference(USER_ID).toString(),
            shrphr.getStringPreference(Token).toString(),
        )
        orderdilivery.enqueue(object : Callback<PrivacyResponse> {
            override fun onResponse(
                call: Call<PrivacyResponse>,
                response: Response<PrivacyResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                firstdata.setText(Html.fromHtml(response!!.body()!!.data.cms_content, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                firstdata.setText(Html.fromHtml(response!!.body()!!.data.cms_content));
                            }
//                            firstdata.text=response!!.body()!!.data.cms_content
                        } else {
                            showToastMessage(this@PrivacyActivity,response!!.body()!!.message)
                        }

                    }else if(response.code()==401){
                        finishAffinity()
                    }else if(response.code()==400){
                        hideProgressDialog()
                        showToastMessage(this@PrivacyActivity,response!!.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<PrivacyResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }
}