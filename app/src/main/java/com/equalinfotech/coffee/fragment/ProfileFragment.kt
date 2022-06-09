package com.equalinfotech.coffee.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.DashBoardActivity
import com.equalinfotech.coffee.Activity.EditProfileActivity
import com.equalinfotech.coffee.Activity.OrderHistoryActivity
import com.equalinfotech.coffee.Adapter.InventryAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.InventoryResponse
import kotlinx.android.synthetic.main.activity_coffee_details.card_topic
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.user_profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
lateinit var deshboardactivity:DashBoardActivity
lateinit var shrphr:shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deshboardactivity=context as DashBoardActivity
        shrphr= shareprefrences(requireActivity())
        inventry()
        card_topic.setBackgroundResource(R.drawable.bottomcorner)
        ext_profile.setOnClickListener {
            startActivity(Intent(activity,EditProfileActivity::class.java))
        }
        orderhistory.setOnClickListener {
            startActivity(Intent(activity,OrderHistoryActivity::class.java))
        }

        if(shrphr.getStringPreference(deshboardactivity.PROFILE_PIC)!!.isNotEmpty()){
            Glide.with(requireActivity()).load(shrphr.getStringPreference(deshboardactivity.PROFILE_PIC))
                .into(user_profile)
        }
        user_name.text=shrphr.getStringPreference(deshboardactivity.FIRST_NAME)
        user_name.text=shrphr.getStringPreference(deshboardactivity.FIRST_NAME)
        user_email.text=shrphr.getStringPreference(deshboardactivity.EMAIL_ID)
        text_phone.text=shrphr.getStringPreference(deshboardactivity.PHONE)
    }


    override fun onResume() {
        super.onResume()
        if(shrphr.getStringPreference(deshboardactivity.PROFILE_PIC)!!.isNotEmpty()){
            Glide.with(requireActivity()).load(shrphr.getStringPreference(deshboardactivity.PROFILE_PIC))
                .into(user_profile)
        }
        user_name.text=shrphr.getStringPreference(deshboardactivity.FIRST_NAME)
        user_name.text=shrphr.getStringPreference(deshboardactivity.FIRST_NAME)
        user_email.text=shrphr.getStringPreference(deshboardactivity.EMAIL_ID)
        text_phone.text=shrphr.getStringPreference(deshboardactivity.PHONE)
        Log.e("DEBUG", "onResume of HomeFragment")

    }


    fun inventry() {
        deshboardactivity.showProgressDialog()
        var orderdilivery: Call<InventoryResponse> = APIUtils.getServiceAPI()!!.inventory(
            shrphr.getStringPreference(deshboardactivity.Token).toString(),
            shrphr.getStringPreference(deshboardactivity.USER_ID).toString()
        )
        orderdilivery.enqueue(object : Callback<InventoryResponse> {
            override fun onResponse(
                call: Call<InventoryResponse>,
                response: Response<InventoryResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        deshboardactivity.hideProgressDialog()
                        invetryrecycle.layoutManager=LinearLayoutManager(context)
                        totalinventry.text=response.body()!!.data.total_quantity
                        invetryrecycle.adapter= context?.let {
                            InventryAdapter(
                                it,
                                response.body()!!.data.inventory as ArrayList<InventoryResponse.Data.Inventory>
                            )
                        }
                        if (response.body()!!.status == "success") {
//                            deshboardactivity.showToastMessage(requireContext(), response.body()!!.message)
                        } else {
                            deshboardactivity.showToastMessage(requireContext(), response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        deshboardactivity.finishAffinity()
                    } else if (response.code() == 400) {
                        deshboardactivity.hideProgressDialog()
                        deshboardactivity.showToastMessage(requireContext(), response.body()!!.message)
                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<InventoryResponse>, t: Throwable) {
                deshboardactivity.hideProgressDialog()
            }
        })
    }




}