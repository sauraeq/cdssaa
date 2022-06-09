package com.equalinfotech.coffee.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.equalinfotech.coffee.R
import kotlinx.android.synthetic.main.activity_order_sucessfull.*

class OrderSucessfullActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_sucessfull)
        close.setOnClickListener {
            startActivity(Intent(this,DashBoardActivity::class.java))
            finishAffinity()
        }
    }
}