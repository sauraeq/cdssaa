package com.equalinfotech.coffee.Activity

import `in`.aabhasjindal.otptextview.OtpTextView
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.OtpResponse
import com.equalinfotech.coffee.modal.SignupResponse
import com.equalinfotech.coffee.util.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : BaseActivity(), View.OnClickListener, cont {
    var RECORD_REQUEST_CODE: Int = 401
    var longitude: String = ""

    lateinit var sharpref: shareprefrences
    var langitude: String = ""
    var fcmtoken: String = ""
    lateinit var data: SignupResponse.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        if (checkPermission() == true) {
            getCurrentlocation()
        } else {
            runtimePermission()
        }
        getToken()
        sharpref = shareprefrences(this)

        txt_signup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        initclick()

    }

    private fun initclick() {
        btnsignup.setOnClickListener(this)
    }


    private fun opendilogfrombottomotp() {
        var botoomshetone = BottomSheetDialog(this, R.style.BottomSheetDialog)
        botoomshetone.setContentView(R.layout.dialog_otp)
        botoomshetone.setCancelable(false)
        botoomshetone.findViewById<TextView>(R.id.otp_contoniew)!!.setOnClickListener {
            if (botoomshetone.findViewById<OtpTextView>(R.id.otp_view)!!.otp.toString().isEmpty()) {
                showToastMessage(this, "Please Enter Otp")
            } else {
                otpverification(botoomshetone.findViewById<OtpTextView>(R.id.otp_view)!!.otp.toString())
            }
        }
        botoomshetone.show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnsignup -> {
                validatedata()
            }
        }
    }

    private fun validatedata() {
        if (txt_username.text.toString().trim().isEmpty()) {
            txt_username.error = "Please Enter Name"
            txt_username.requestFocus()
        } else if (txt_phonenumber.text.toString().trim().isEmpty()) {
            txt_phonenumber.error = "Please Enter Phone Number"
            txt_phonenumber.requestFocus()
        } else if (txt_phonenumber.text.toString().trim().length != 10) {
            txt_phonenumber.error = "Please Enter Vaild Phone Number"
            txt_phonenumber.requestFocus()
        } else if (txt_email.text.toString().trim().isEmpty()) {
            txt_email.error = "Please Enter Email Id"
            txt_email.requestFocus()
        } else if (!emailValidator(txt_email.text.toString())) {
            txt_email.error = "Please Enter Vaild Email Id"
            txt_email.requestFocus()
        } else if (txt_password.text.toString().trim().isEmpty()) {
            txt_password.error = "Please Enter Password"
            txt_password.requestFocus()
        } else if (txt_password.text.toString().trim().isEmpty()) {
            txt_password.error = "Please Enter Password"
            txt_password.requestFocus()
        }else if (txt_password.text.toString().trim().length<6) {
            txt_password.error = " Password Must be Six Digit"
            txt_password.requestFocus()
        } else {
            signup()
        }
    }


    fun signup() {
        showProgressDialog()

        var orderdilivery: Call<SignupResponse> = APIUtils.getServiceAPI()!!.signup(
            txt_username.text.toString(),
            txt_email.text.toString(),
            txt_password.text.toString(),
            txt_phonenumber.text.toString(),
            "1",
            langitude,
            longitude,
            fcmtoken
        )
        orderdilivery.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            data = response.body()!!.data
                            opendilogfrombottomotp()
                        } else {
                            showToastMessage(this@SignupActivity, response.body()!!.message)
                        }
                    } else if (response.code() == 401) {
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@SignupActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun otpverification(otp: String) {
        showProgressDialog()

        var orderdilivery: Call<OtpResponse> = APIUtils.getServiceAPI()!!.emailVerification(
            data.id,
            data.token,
            otp,
        )
        orderdilivery.enqueue(object : Callback<OtpResponse> {
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            sharpref.setStringPreference(Token, data.token)
                            sharpref.setStringPreference(USER_ID, data.id)
                            sharpref.setStringPreference(FIRST_NAME, data.name)
                            sharpref.setStringPreference(EMAIL_ID, data.email)
                            sharpref.setStringPreference(PHONE, data.mobileNumber)
                            sharpref.setStringPreference(PROFILE_PIC, data.profileImage)

                            startActivity(
                                Intent(
                                    this@SignupActivity,
                                    DashBoardActivity::class.java
                                )
                            )
                            finishAffinity()

                        } else {
                            showToastMessage(this@SignupActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@SignupActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    fun runtimePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), RECORD_REQUEST_CODE
        )
    }

    //////======if permission granted=====
    fun checkPermission(): Boolean? {
        val FirstPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val SecondPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val thirdPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val FourPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val FivePermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && thirdPermissionResult == PackageManager.PERMISSION_GRANTED && FourPermissionResult == PackageManager.PERMISSION_GRANTED && FirstPermissionResult == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val camerapermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val WriteStoragePermission =
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                    val ReadStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED
                    val FineLocationPermission =
                        grantResults[3] == PackageManager.PERMISSION_GRANTED
                    val CoarsePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED
                    if (camerapermission && WriteStoragePermission && ReadStoragePermission && FineLocationPermission && CoarsePermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
                return
            }
        }
    }

////==========end permission=====


    ////==========end permission=====
    fun getCurrentlocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        /* val locationListener: LocationListener = object : LocationListener() {
             fun onLocationChanged(location: Location) {
                 val latitute: Double = location.getLatitude()
                 val longitute: Double = location.getLongitude()
                 Log.e("lati", latitute.toString())
                 Log.e("long", longitute.toString())
             }

             fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
             fun onProviderEnabled(provider: String?) {}
             fun onProviderDisabled(provider: String?) {}
         }*/

        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                longitude = location.longitude.toString()
                langitude = location.latitude.toString()
                Log.e("langitude", location.longitude.toString())
                Log.e("langitude", location.latitude.toString())

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                500,
                0f,
                locationListener
            )
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }


    private fun getToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            fcmtoken = task.result

//            SharedPreferenceUtils.getInstance(this)
//                .setValue(ConstantUtils.FCM_TOKEN, token)
//            val postFcmTokenData = HashMap<String,String>()
//            postFcmTokenData.put("user_id",SharedPreferenceUtils.getInstance(mContext).getStringValue(ConstantUtils.USER_ID,""))
//            postFcmTokenData.put("fcmToken",token!!)
//            updateToken(postFcmTokenData,dialog)
        })
    }


    override fun onResume() {
        super.onResume()
        if (checkPermission() == true) {
            getCurrentlocation()
        } else {
            runtimePermission()
        }
    }
}


