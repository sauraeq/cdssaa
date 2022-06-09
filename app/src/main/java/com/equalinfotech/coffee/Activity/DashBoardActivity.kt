package com.equalinfotech.coffee.Activity

import android.Manifest
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.fragment.HomeFragment
import com.equalinfotech.coffee.fragment.ProfileFragment
import com.equalinfotech.coffee.menu.ListItemRow
import com.equalinfotech.coffee.menu.ListItemRowAdapter
import com.equalinfotech.coffee.modal.CartCountResponse
import com.equalinfotech.coffee.util.BaseActivity
import com.equalinfotech.learnorteach.constant.cont
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.learnorteach.notification.NotificationActivity
import com.github.javiersantos.appupdater.AppUpdater
import com.waterdelivery.Serivice.LocationService
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_dash_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DashBoardActivity : BaseActivity(), View.OnClickListener, cont,
    AdapterView.OnItemClickListener {
    lateinit var shrpre: shareprefrences
    val titles = arrayOf(
        R.string.home,//1
        R.string.profile,
        R.string.how_its_work,//2
        R.string.membership,
        R.string.privacy_policy,
        R.string.help_faq,
        R.string.contect_us,
        R.string.logout
    )

    val images = arrayOf<Int>(
        R.mipmap.home,
        R.drawable.userblack,
        R.mipmap.howitwork,
        R.mipmap.membership,
        R.mipmap.privacypolicy,
        R.mipmap.help,
        R.mipmap.conatct,
        R.mipmap.logout,
    )
    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        val appUpdater = AppUpdater(this)
        appUpdater.start()
        startLocationService()
        shrpre = shareprefrences(this)
        drawer.onItemClickListener = this
        listImplement()
        rlRight.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        initclick()
        callfragemnt(HomeFragment())
        dataset()
    }

    private fun dataset() {
        if (shrpre.getStringPreference(PROFILE_PIC)!!.isNotEmpty()) {
            Glide.with(this).load(shrpre.getStringPreference(PROFILE_PIC))
                .into(profile_user)
        }
        tvUserName.text = shrpre.getStringPreference(FIRST_NAME)
        tvUserEmail.text = shrpre.getStringPreference(EMAIL_ID)
    }

    private fun initclick() {
        openDrawerhome.setOnClickListener(this)
        img_notification.setOnClickListener(this)
        profiletab.setOnClickListener(this)
        hometab.setOnClickListener(this)
        barcaode.setOnClickListener(this)
    }

    private fun listImplement() {
        val rowItems: MutableList<ListItemRow> =
            ArrayList<ListItemRow>()
        for (i in 0 until titles.size) {
            val item = ListItemRow(
                images.get(i),
                titles.get(i)
            )
            rowItems.add(item)
        }
        val listItemRowAdapter = ListItemRowAdapter(
            applicationContext,
            R.layout.list_view_item_row, rowItems
        )

        drawer.adapter = listItemRowAdapter
        /* supportFragmentManager.beginTransaction().replace(R.id.frame,
             HomeFragment()
         ).commit()*/
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.openDrawerhome -> {
                openDrawer()
            }
            R.id.img_notification -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
            R.id.profiletab -> {
                callfragemnt(ProfileFragment())
            }
            R.id.hometab -> {
                callfragemnt(HomeFragment())
            }
            R.id.barcaode -> {
                startActivity(Intent(this, BarCodeActivity::class.java))
            }
        }
    }

    fun openDrawer() {
        if (!draweLayout.isDrawerOpen(drawerContainer)) {
            draweLayout.openDrawer(drawerContainer)
        } else {
            draweLayout.closeDrawer(drawerContainer)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                callfragemnt(HomeFragment())
                draweLayout.closeDrawer(drawerContainer)
            }
            1 -> {
                draweLayout.closeDrawer(drawerContainer)
                callfragemnt(ProfileFragment())
            }
            2 -> {
                draweLayout.closeDrawer(drawerContainer)
                startActivity(Intent(this, HowItWorkActivity::class.java))
            }
            3 -> {
                draweLayout.closeDrawer(drawerContainer)
                startActivity(Intent(this, MembershipPlanActivity::class.java))
            }
            4 -> {
                draweLayout.closeDrawer(drawerContainer)
                startActivity(Intent(this, PrivacyActivity::class.java))

            }
            5 -> {
                draweLayout.closeDrawer(drawerContainer)
                startActivity(Intent(this, FaqActivity::class.java))
            }
            6 -> {
                draweLayout.closeDrawer(drawerContainer)
                startActivity(Intent(this, ContactActivity::class.java))
            }
            7 -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
                shrpre.clearAllPreferences()
            }
        }
    }


    fun callfragemnt(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.Frame, fragment)
            .commit()
    }


    override fun onResume() {
        super.onResume()
        dataset()
        count()
    }


    fun count() {
        var orderdilivery: Call<CartCountResponse> = APIUtils.getServiceAPI()!!.cartItemCount(
            shrpre.getStringPreference(Token).toString(),
            shrpre.getStringPreference(USER_ID).toString(),
        )
        orderdilivery.enqueue(object : Callback<CartCountResponse> {
            override fun onResponse(
                call: Call<CartCountResponse>,
                response: Response<CartCountResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            cart_badge.text = response.body()!!.data.cart_quantity.toString()
                        } else {

                        }
                    } else if (response.code() == 401) {
                        startActivity(Intent(this@DashBoardActivity, LoginActivity::class.java))
                        finishAffinity()
                    } else if (response.code() == 400) {
                        hideProgressDialog()
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<CartCountResponse>, t: Throwable) {
                hideProgressDialog()
            }

        })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun startLocationService() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        } else {
            if (!isLocationServiceRunning()) {
                val intent1 = Intent(applicationContext, LocationService::class.java)
                intent1.action = "startlocationservice"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startService(intent1)
                }
            }
        }
    }


    private fun isLocationServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (LocationService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }
}