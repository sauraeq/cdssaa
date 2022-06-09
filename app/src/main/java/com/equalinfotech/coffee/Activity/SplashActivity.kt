package com.equalinfotech.coffee.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.R

class SplashActivity : AppCompatActivity(), cont {
    var RECORD_REQUEST_CODE: Int = 401
    lateinit var shr: shareprefrences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        shr = shareprefrences(this)
        if (checkPermission() == true) {
        } else {
            runtimePermission()
        }
        Handler().postDelayed({
            if (shr.getStringPreference(Token)!!.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, DashBoardActivity::class.java))
                finish()
            }
        }, 3000.toLong())
    }


    fun runtimePermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE
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
        val  SixPermissionResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && thirdPermissionResult == PackageManager.PERMISSION_GRANTED && FourPermissionResult == PackageManager.PERMISSION_GRANTED && FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SixPermissionResult==PackageManager.PERMISSION_GRANTED
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
                        runtimePermission()
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
                return
            }
        }
    }
}