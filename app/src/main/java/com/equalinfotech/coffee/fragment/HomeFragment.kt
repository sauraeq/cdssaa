package com.equalinfotech.coffee.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.GetMyDoctor.doctorfinder.home.adpater.SliderAdpater
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.DashBoardActivity
import com.equalinfotech.coffee.Activity.LoginActivity
import com.equalinfotech.coffee.Adapter.HomeAdapter
import com.equalinfotech.coffee.Adapter.ImageSlideAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.HomeScreenReponse
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    lateinit var dashBoardActivity: DashBoardActivity
    lateinit var sharprf: shareprefrences
    private var handler: Handler? = null
    private val ANIM_VIEWPAGER_DELAY: Long = 10000
    private val ANIM_VIEWPAGER_DELAY_USER_VIEW: Long = 10000
    private var animateViewPager: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashBoardActivity = context as DashBoardActivity
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
                            coffielistrecycle.layoutManager = GridLayoutManager(activity, 3)
                            coffielistrecycle.adapter =
                                activity?.let { HomeAdapter(it, response.body()!!.data.hostArr) }
//                            setAdsData(response!!.body()!!.data.slider_list)
                            if (response.body()!!.data.slider_list != null) {
                                var sliderAdpater =
                                    SliderAdpater(
                                        context,
                                        response.body()!!.data.slider_list as ArrayList<HomeScreenReponse.Data.Slider>
                                    )
                                view_pager.adapter = sliderAdpater
                                dots_indicator.setViewPager(view_pager)
                            }
                            if (response.body()!!.data.hostArr.size > 0) {
                                errorhome.visibility = View.GONE
                            } else {
                                errorhome.visibility = View.VISIBLE
                            }
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

    private fun setAdsData(adsData: List<HomeScreenReponse.Data.Slider>) {
        view_pager.adapter = ImageSlideAdapter(
            activity, adsData, object : ImageSlideAdapter.OnBannerItemClick {
                override fun onBannerImageClick(data: String?) {}
            })
        dots_indicator.setViewPager(view_pager)
        runnable(adsData.size)
        animateViewPager?.let {
            Handler().postDelayed(
                it,
                ANIM_VIEWPAGER_DELAY_USER_VIEW
            )
        }
        // addBottomDots(linearLayout, adsData.size(), 0);
    }


    fun runnable(size: Int) {
        handler = Handler()
        try {
            animateViewPager = Runnable {
                if (view_pager.currentItem != null) {
                    if (view_pager.currentItem == size - 1) {
                        view_pager.currentItem = 0
                    } else {
                        view_pager.setCurrentItem(
                            view_pager.currentItem + 1, true
                        )
                    }
                    animateViewPager?.let { Handler().postDelayed(it, ANIM_VIEWPAGER_DELAY) }
                }
            }
        } catch (e: Exception) {

        }

    }


}