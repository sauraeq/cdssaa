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
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.ForegetPasswordResponse
import com.equalinfotech.coffee.modal.LoginResponse
import com.equalinfotech.coffee.modal.OtpResponse
import com.equalinfotech.coffee.util.BaseActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.gogo.gogokull.utils.ConstantUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity(), View.OnClickListener, cont {
    lateinit var shrpre: shareprefrences
    var sign_type: Int = 1
    var RECORD_REQUEST_CODE: Int = 401
    var password: String = ""
    private val RC_SIGN_IN = 234
    var passwordimagepassword=false
    var confirmmpasswordimagepassword=false
    var facebook_id: String = ""
    lateinit var email: String
     var username: String=""
    var longitude: String = ""
    var langitude: String = ""
    var google_id: String = ""
    var fcmtoken:String=""
    lateinit var mAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var forgetdata: ForegetPasswordResponse.Data

        var LoginManager: LoginManager = com.facebook.login.LoginManager.getInstance()
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (checkPermission() == true) {
            getCurrentlocation()

        } else {
            runtimePermission()
        }
        shrpre = shareprefrences(this)

        getToken()

        initclick()
//        ................Google Sign In..............
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

//        =============Facebookkkkkkkkkk==============

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        facebok_login_button.setOnClickListener {
            sign_type = 3
            com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(
                this,
                arrayListOf("email", "public_profile")
            )

            LoginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    handleFacebookToken(loginResult!!.accessToken)
                    Toast.makeText(this@LoginActivity, "sucessmsg", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    Log.e("exception", exception.message.toString())
                }
            })
        }

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                updateUI(user)
            } else {
                updateUI(null)
            }
        }

        ////////===========facebokkkkkkk------------


        img_google_login.setOnClickListener {
            sign_type = 2
            googleSign()
        }


    }

    private fun initclick() {
        forgetpassword.setOnClickListener(this)
        txt_signup.setOnClickListener(this)
        txt_signin.setOnClickListener(this)
    }


    private fun opendilogfrombottom() {
        var botoomshet = BottomSheetDialog(this, R.style.BottomSheetDialog)
        botoomshet.setContentView(R.layout.dialog_forgetpasswordfirstscreen)
//        botoomshet.setCancelable(false)
        botoomshet.findViewById<TextView>(R.id.email_conirm)!!.setOnClickListener {
            if (botoomshet.findViewById<EditText>(R.id.edt_email)!!.text.toString().isEmpty()) {
                showToastMessage(this, "Enter Username")
            } else if (!emailValidator(botoomshet.findViewById<EditText>(R.id.edt_email)!!.text.toString())) {
                showToastMessage(this, "Enter Valid Email-Id")
            } else {
                forgetpassword(
                    botoomshet.findViewById<EditText>(R.id.edt_email)!!.text.toString(),
                    botoomshet
                )
            }
        }
        botoomshet.show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.forgetpassword -> {
                opendilogfrombottom()
            }
            R.id.txt_signup -> {
                startActivity(Intent(this, SignupActivity::class.java))
            }
            R.id.txt_signin -> {
                sign_type = 1
                validation()
//                startActivity(Intent(this,DashBoardActivity::class.java))
            }
        }
    }

    fun validation() {
        email = txt_username.text.toString().trim()
        password = txt_password.text.toString()

        if (email.isEmpty()) {
            txt_username.error = "Enter your Email"
            txt_username.requestFocus()
        } else if (!emailValidator(email)) {
            txt_username.error = "Enter Vaild Email"
            txt_username.requestFocus()
        } else if (password.isEmpty()) {
            txt_password.error = "Enter your Password"
            txt_password.requestFocus()
        } else {
            signin()
            // user_login_withfirebase()
        }
    }

    private fun opendilogfrombottomotp() {
        var botoomshetone = BottomSheetDialog(this, R.style.BottomSheetDialog)
        botoomshetone.setContentView(R.layout.dialog_otp)
//        botoomshetone.setCancelable(false)
        botoomshetone.findViewById<TextView>(R.id.otp_contoniew)!!.setOnClickListener {
            if (botoomshetone.findViewById<OtpTextView>(R.id.otp_view)!!.otp.toString().isEmpty()) {
                showToastMessage(this, "Please Enter Otp")
            } else {
                otpverification(
                    botoomshetone.findViewById<OtpTextView>(R.id.otp_view)!!.otp.toString(),
                    botoomshetone,
                    )
            }


        }
        botoomshetone.show()
    }


    private fun opendilogfrombottomresetpassword() {
        var botoomshettwo = BottomSheetDialog(this, R.style.BottomSheetDialog)
        botoomshettwo.setContentView(R.layout.dialog_resetpassword)
        botoomshettwo.setCancelable(true)
        botoomshettwo.findViewById<ImageView>(R.id.passwordimage)!!.setOnClickListener {
            if (passwordimagepassword) {
                botoomshettwo.findViewById<EditText>(R.id.edt_password)!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordimagepassword = false;
                botoomshettwo.findViewById<ImageView>(R.id.passwordimage)!!.setImageResource(R.drawable.eye);

            }
            else {
                botoomshettwo.findViewById<EditText>(R.id.edt_password)!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD;
                passwordimagepassword = true;
                botoomshettwo.findViewById<ImageView>(R.id.passwordimage)!!.setImageResource(R.drawable.eyecross);

            }

        }
        botoomshettwo.findViewById<ImageView>(R.id.confrimpasswordimage)!!.setOnClickListener {
            if (confirmmpasswordimagepassword) {
                botoomshettwo.findViewById<EditText>(R.id.txt_confirmpassword)!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                confirmmpasswordimagepassword = false;
                botoomshettwo.findViewById<ImageView>(R.id.confrimpasswordimage)!!.setImageResource(R.drawable.eye);

            }
            else {
                botoomshettwo.findViewById<EditText>(R.id.txt_confirmpassword)!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD;
                confirmmpasswordimagepassword = true;
                botoomshettwo.findViewById<ImageView>(R.id.confrimpasswordimage)!!.setImageResource(R.drawable.eyecross);

            }

        }
        botoomshettwo.findViewById<TextView>(R.id.reset_continue)!!.setOnClickListener {
            if (botoomshettwo.findViewById<EditText>(R.id.edt_password)!!.text.toString().trim()
                    .isEmpty()
            ) {
                showToastMessage(this, "Please Enter Password")
            } else if (botoomshettwo.findViewById<EditText>(R.id.txt_confirmpassword)!!.text.toString()
                    .trim().isEmpty()
            ) {
                showToastMessage(this, "Please Enter Confirm Password")
            } else if (botoomshettwo.findViewById<EditText>(R.id.txt_confirmpassword)!!.text.toString() != botoomshettwo.findViewById<EditText>(
                    R.id.edt_password
                )!!.text.toString()
            ) {
                showToastMessage(this, "Password & Confirm Password Does Not Match")
            } else {
                resetpassword(
                    botoomshettwo.findViewById<EditText>(R.id.edt_password)!!.text.toString(),
                    botoomshettwo,
                )
            }
            botoomshettwo.dismiss()
        }
        botoomshettwo.show()
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            username = user.displayName.toString()
            email = user.email.toString()
            facebook_id = user.uid.toString()
            Log.e("username", facebook_id.toString())
            signin()
        }
    }


    /////////////===========End Google===========
    fun handleFacebookToken(token: AccessToken) {
//        Log.d(TAG, "handleFacebookToken" + token)
        var credentail: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credentail).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "signInWithCredential:success")
                val user = mAuth.currentUser
                Log.e("useraa", user!!.email.toString())
                updateUI(user)
            } else {
//                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }

            // ...
        }

    }


    ////===================Google==============
    private fun googleSign() {
        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResulyt(task)

            /*try {

                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }*/
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResulyt(completedTask: Task<GoogleSignInAccount>?) {

        try {

            var account = completedTask?.getResult(ApiException::class.java)

            Toast.makeText(this, "Sign in sucessfully", Toast.LENGTH_SHORT).show()

            FirebaseGoogleAuth(account)

        } catch (e: ApiException) {
            FirebaseGoogleAuth(null)
            Toast.makeText(this, "Sign in Unsucessfully", Toast.LENGTH_SHORT).show()
        }

    }

    private fun FirebaseGoogleAuth(account: GoogleSignInAccount?) {
        try{
            var authCredential = GoogleAuthProvider.getCredential(account?.idToken, null)
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, OnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Sucessfully", Toast.LENGTH_SHORT).show()
                    var user = mAuth.currentUser
                    UpdateUI(user)
                } else {
                    UpdateUI(null)
                    //  Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
            })
        }catch (e:Exception){

        }


    }

    private fun UpdateUI(user: FirebaseUser?) {
        var account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) {
            username = account.displayName.toString()
            var personGivenName = account.givenName
            var personFamilyName = account.familyName
            email = account.email.toString()
            google_id = account.id.toString()
            var personPicture = account.photoUrl
            Log.e("account", account.toString())
            Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
            signin()

        }

    }

    /////////////===========End Google===========


    fun forgetpassword(otp: String, bottomSheetDialog: BottomSheetDialog) {
        showProgressDialog()

        var orderdilivery: Call<ForegetPasswordResponse> =
            APIUtils.getServiceAPI()!!.forgotPassword(
                otp,
            )
        orderdilivery.enqueue(object : Callback<ForegetPasswordResponse> {
            override fun onResponse(
                call: Call<ForegetPasswordResponse>,
                response: Response<ForegetPasswordResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                            bottomSheetDialog.dismiss()
                            forgetdata = response.body()!!.data
                            opendilogfrombottomotp()

                        } else {
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@LoginActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ForegetPasswordResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun otpverification(otp: String, bottomSheetDialog: BottomSheetDialog) {
        showProgressDialog()

        var orderdilivery: Call<OtpResponse> = APIUtils.getServiceAPI()!!.emailVerification(
            forgetdata.consumerId,
            forgetdata.token,
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
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                            bottomSheetDialog.dismiss()
                            opendilogfrombottomresetpassword()

                        } else {
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@LoginActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun resetpassword(passwordddd: String, bottomSheetDialog: BottomSheetDialog) {
        showProgressDialog()

        var orderdilivery: Call<OtpResponse> = APIUtils.getServiceAPI()!!.resetPassword(
            forgetdata.consumerId,
            passwordddd,
            passwordddd,
            forgetdata.token,
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
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                            bottomSheetDialog.dismiss()


                        } else {
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@LoginActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun signin() {
        showProgressDialog()
        var orderdilivery: Call<LoginResponse> = APIUtils.getServiceAPI()!!.login(
            email,
            sign_type.toString(),
            password,
            google_id,
            facebook_id,
            username,
            langitude,
            longitude,
            fcmtoken
        )
        orderdilivery.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            var data: LoginResponse.Data = response.body()!!.data
                            shrpre.setStringPreference(Token, data.token)
                            shrpre.setStringPreference(USER_ID, data.id)
                            shrpre.setStringPreference(FIRST_NAME, data.name)
                            shrpre.setStringPreference(EMAIL_ID, data.email)
                            shrpre.setStringPreference(PHONE, data.mobileNumber)
                            shrpre.setStringPreference(PROFILE_PIC, data.profileImage)
                            startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
                            finishAffinity()


                        } else {
                            showToastMessage(this@LoginActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@LoginActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
            Log.d("token", "getToken: $fcmtoken")
            shrpre.setStringPreference(ConstantUtils.FCM_TOKEN,fcmtoken)
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