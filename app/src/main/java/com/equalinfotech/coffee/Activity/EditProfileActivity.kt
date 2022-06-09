package com.equalinfotech.coffee.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.PictureResponse
import com.equalinfotech.coffee.modal.ProfileDataUpdationReponse
import com.equalinfotech.coffee.modal.ProfileResponse
import com.equalinfotech.coffee.util.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.user_profile
import kotlinx.android.synthetic.main.activity_privacy.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

class EditProfileActivity : BaseActivity(), cont {
    lateinit var sharprf: shareprefrences
    val RECORD_REQUEST_CODE = 101
    private val TAG = "PermissionDemo"
    var imageUriSign: Uri? = null
    var mFilePathSign = ""
    var picname: String? = null
    var profileimageString: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharprf = shareprefrences(this)
        getprofile()
        img_take_pic.setOnClickListener(View.OnClickListener {
            picname = "profilepic"
            if (checkpermission()!!) {
                selectImage(this)
            } else {
                setupPermissions()
            }
        })


        edit_profile_back.setOnClickListener {
            onBackPressed()
        }
        txt_update_profile.setOnClickListener {
            if (edt_phone_number.text.toString().trim().isEmpty()) {
                edt_phone_number.error = "Please Enter Phone Number"
                edt_phone_number.requestFocus()
            } else if (edt_phone_number.text.toString().trim().length != 10) {
                edt_phone_number.error = "Please Enter Vaild Phone Number"
                edt_phone_number.requestFocus()
            } else {
                updateprofiledata()
            }
        }
    }

    //=============Image take================
    private fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 0)

            } else if (options[item] == "Choose from Gallery") {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 ->
                    if (data != null) {
                        var datacamera = data.extras!!.get("data") as Bitmap
                        imageUriSign = getImageUri(this, datacamera)
                        mFilePathSign = getAbsolutePath(imageUriSign)
                        Log.e("paths", mFilePathSign)
                        Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                        if (picname == "profilepic") {
                            user_profile.setImageBitmap(datacamera)
                            val baos = ByteArrayOutputStream()
                            datacamera.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val images = baos.toByteArray()
                            profileimageString = Base64.encodeToString(images, Base64.DEFAULT)
                        }
                    }
                1 ->
                    try {
                        imageUriSign = data!!.data
                        val imageStream4: InputStream =
                            contentResolver.openInputStream(imageUriSign!!)!!
                        val selectedImage4 = BitmapFactory.decodeStream(imageStream4)
                        val selectedImageUri = data.data
                        val filePath = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = contentResolver.query(
                            selectedImageUri!!,
                            filePath, null, null, null
                        )
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePath[0])
                        mFilePathSign = cursor.getString(columnIndex)
                        Log.e("paths", mFilePathSign)
                        cursor.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                    }
            }
            updateProfile()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)


        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "IMG_" + Calendar.getInstance().time,
            null
        )
        return Uri.parse(path)
    }

    fun getAbsolutePath(uri: Uri?): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(uri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    fun checkpermission(): Boolean? {
        val camerapermission =
            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA))
        return camerapermission == PackageManager.PERMISSION_GRANTED
    }

    fun setupPermissions() {
        val permissions =
            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA))
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), RECORD_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Log.i(TAG, "Permission has been granted by user")
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Log.i(TAG, "Permission has been denied by user")
                }
                return
            }
        }
    }

    fun updateProfile() {
        showProgressDialog()

        var userid = sharprf.getStringPreference(USER_ID)

        val multiPartRepeatString = "application/image"
        var facility_image: MultipartBody.Part? = null


        val user_id: RequestBody = userid.toString().toRequestBody(MultipartBody.FORM)
        val token: RequestBody =
            sharprf.getStringPreference(Token).toString().toRequestBody(MultipartBody.FORM)



        if (imageUriSign != null && imageUriSign!!.path != null) {
            val file = File(mFilePathSign)
            val signPicBody = file.asRequestBody(multiPartRepeatString.toMediaTypeOrNull())
            facility_image =
                MultipartBody.Part.createFormData("profileImage", file.name, signPicBody)
            // val signPicBody = RequestBody.create(parse.parse(multiPartRepeatString), file)

            //facility_image = createFormData.createFormData("profile_image", file.name, signPicBody)
        }


        var updateprofile: Call<PictureResponse> = APIUtils.getServiceAPI()!!.updateProfileUser(
            user_id,
            token,
            facility_image
        )
        updateprofile.enqueue(object : Callback<PictureResponse> {
            override fun onResponse(
                call: Call<PictureResponse>,
                response: Response<PictureResponse>
            ) {
                hideProgressDialog()
                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            Glide.with(this@EditProfileActivity)
                                .load(response.body()!!.data.profileImage)
                                .into(user_profile)
                            sharprf.setStringPreference(
                                PROFILE_PIC,
                                response.body()!!.data.profileImage
                            )
                            showToastMessage(this@EditProfileActivity, response.body()!!.message)

                        } else {
                            showToastMessage(this@EditProfileActivity, response.body()!!.message)

                        }

                    } else if (response.code() == 401) {

                        sharprf.clearAllPreferences()
                        startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
                        finishAffinity()

                    }
                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<PictureResponse>, t: Throwable) {
                hideProgressDialog()
                Toast.makeText(this@EditProfileActivity, t.message, Toast.LENGTH_SHORT).show()

            }

        })

    }


    fun getprofile() {
        showProgressDialog()

        var orderdilivery: Call<ProfileResponse> = APIUtils.getServiceAPI()!!.get_profile(
            sharprf.getStringPreference(USER_ID).toString(),
            sharprf.getStringPreference(Token).toString(),
        )
        orderdilivery.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            txt_user_name.text = response.body()!!.data.name
                            edt_user_name.setText(response.body()!!.data.name)
                            edt_Email.setText(response.body()!!.data.email)
                            edt_phone_number.setText(response.body()!!.data.mobileNumber)
                            if (response.body()!!.data.profileImage.isNotBlank()) {
                                Glide.with(this@EditProfileActivity)
                                    .load(response.body()!!.data.profileImage)
                                    .into(user_profile)
                            }

                        } else {
                            showToastMessage(this@EditProfileActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {


                        finishAffinity()


                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@EditProfileActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }


    fun updateprofiledata() {
        showProgressDialog()

        var orderdilivery: Call<ProfileDataUpdationReponse> =
            APIUtils.getServiceAPI()!!.editProfile(
                sharprf.getStringPreference(USER_ID).toString(),
                edt_user_name.text.toString().trim(),
                edt_Email.text.toString().trim(),
                edt_phone_number.text.toString().trim(),
                sharprf.getStringPreference(Token).toString(),
            )
        orderdilivery.enqueue(object : Callback<ProfileDataUpdationReponse> {
            override fun onResponse(
                call: Call<ProfileDataUpdationReponse>,
                response: Response<ProfileDataUpdationReponse>
            ) {

                try {

                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            showToastMessage(this@EditProfileActivity, response.body()!!.message)
                            sharprf.setStringPreference(
                                PHONE,
                                edt_phone_number.text.toString().trim()
                            )

                        } else {
                            showToastMessage(this@EditProfileActivity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                        showToastMessage(this@EditProfileActivity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<ProfileDataUpdationReponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

}