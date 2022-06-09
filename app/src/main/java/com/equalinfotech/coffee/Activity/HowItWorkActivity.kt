package com.equalinfotech.coffee.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.equalinfotech.coffee.R
import kotlinx.android.synthetic.main.activity_how_it_work.*

class HowItWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_it_work)
        tv_backwork.setOnClickListener {
            onBackPressed()
        }
    }
}