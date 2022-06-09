package com.equalinfotech.coffee.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.DataResponse
import com.equalinfotech.coffee.util.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwishActivity : BaseActivity() {
    lateinit var token:String
    lateinit var callbackurl:String
    private lateinit var stringStringHashMap: HashMap<String, String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swish)
        stringStringHashMap = HashMap()



    }




    fun openSwishWithToken(context: Context?, token: String?, callBackUrl: String?): Boolean {
        if (token == null || token.length == 0 || callBackUrl == null || callBackUrl.length == 0 || context == null) {
            return false
        }
        // Construct the uri
        // Note that appendQueryParameter takes care of uri encoding
        // the parameters
        val url: Uri = Uri.Builder()
            .scheme("swish")
            .authority("paymentrequest")
            .appendQueryParameter("token", token)
            .appendQueryParameter("callbackurl", callBackUrl)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.setPackage("se.bankgirot.swish.sandbox")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("errorrr", e.toString())
            // Unable to start Swish return false;
        }
        return true
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Toast.makeText(this, intent.toString(), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "back to on back", Toast.LENGTH_SHORT).show()
    }

}