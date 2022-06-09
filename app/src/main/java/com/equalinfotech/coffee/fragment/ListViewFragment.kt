package com.equalinfotech.coffee.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.HomeDetailsActivity
import com.equalinfotech.coffee.Activity.LoginActivity
import com.equalinfotech.coffee.Adapter.ListViewAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.HomeScreenReponse
import kotlinx.android.synthetic.main.fragment_list_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListViewFragment : Fragment() {
    lateinit var dashBoardActivity: HomeDetailsActivity
    lateinit var sharprf: shareprefrences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashBoardActivity = context as HomeDetailsActivity
        sharprf = shareprefrences(requireActivity())
        homescreendata()

    }


    fun homescreendata() {
        dashBoardActivity.showProgressDialog()

        var orderdilivery: Call<HomeScreenReponse> = APIUtils.getServiceAPI()!!.homeScreen(
            sharprf.getStringPreference(dashBoardActivity.Token).toString(),
            sharprf.getStringPreference(dashBoardActivity.USER_ID).toString(),
        )
        orderdilivery.enqueue(object : Callback<HomeScreenReponse> {
            override fun onResponse(
                call: Call<HomeScreenReponse>,
                response: Response<HomeScreenReponse>
            ) {

                try {

                    if (response.code() == 200) {
                        dashBoardActivity.hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            list_recycle.layoutManager = LinearLayoutManager(activity)
                            list_recycle.adapter =
                                ListViewAdapter(requireActivity(), response.body()!!.data.hostArr)
                        } else {
                            dashBoardActivity.showToastMessage(activity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        startActivity(Intent(activity, LoginActivity::class.java))
                        dashBoardActivity.finishAffinity()


                    } else if (response.code() == 400) {
                        dashBoardActivity.hideProgressDialog()
                        dashBoardActivity.showToastMessage(activity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<HomeScreenReponse>, t: Throwable) {
                dashBoardActivity.hideProgressDialog()
            }

        })
    }


}