package com.equalinfotech.coffee.Activity

import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.equalinfotech.coffee.R
import kotlinx.android.synthetic.main.activity_contact.*


class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        card_topic.setBackgroundResource(R.drawable.cardsky_background)
        tv_backcontactus.setOnClickListener {
            onBackPressed()
        }
        contact_tomail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = ClipDescription.MIMETYPE_TEXT_PLAIN
            intent.putExtra(Intent.EXTRA_EMAIL,"support@g12ablesolutions.se" )
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject for email")
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Description for email")
            startActivity(Intent.createChooser(intent,"Send Email"))

        }
        conbtact_tophone.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + 4677179336) //change the number

            startActivity(callIntent)
        }
    }
}