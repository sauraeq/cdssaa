package com.GetMyDoctor.doctorfinder.home.adpater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

import com.bumptech.glide.Glide
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.modal.HomeScreenReponse

import kotlinx.android.synthetic.main.slider_layout.view.*


class SliderAdpater(val context: Context?, var sliderList:ArrayList<HomeScreenReponse.Data.Slider>): PagerAdapter() {

    private var inflater: LayoutInflater? = null


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val view: View = layoutInflater.inflate(R.layout.slider_layout, null)
        var id=sliderList[position].slider_id
        Log.e("id",id)
        var url="https://getmydoctor.in/panel/"
        var imagename=sliderList[position].image
        Log.e("image",imagename)
        var image=sliderList[position].image
        Log.e("image",image)
        Glide.with(context!!).load(image).into(view.slider_imageView)
//        Picasso.get().load(image).into(view.slider_imageView)



      //  view.slider_imageView.setImageResource(images[position])
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view


    }

    override fun getCount(): Int {
       return  sliderList.size
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}