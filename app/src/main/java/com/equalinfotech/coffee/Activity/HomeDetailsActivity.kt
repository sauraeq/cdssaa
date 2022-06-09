package com.equalinfotech.coffee.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.coffee.Adapter.MapiFragment
import com.equalinfotech.coffee.Adapter.ViewpagerAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.fragment.ListViewFragment
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_home_details.*

class HomeDetailsActivity : BaseActivity(),cont {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_details)
        initclick()
        callfragment(ListViewFragment())
        setbackgroundblue(listview)
        tv_backnhomedetails.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initclick() {
        listview.setOnClickListener {
            callfragment(ListViewFragment())
            setbackgroundblue(listview)
            setbackgroundnormal(mapview)
        }
        mapview.setOnClickListener {
            callfragment(MapiFragment())
            setbackgroundblue(mapview)
            setbackgroundnormal(listview)
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
}