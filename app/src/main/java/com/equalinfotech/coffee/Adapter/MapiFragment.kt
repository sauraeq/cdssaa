package com.equalinfotech.coffee.Adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.HomeDetailsActivity
import com.equalinfotech.coffee.Activity.LoginActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.HomeScreenReponse
import com.equalinfotech.coffee.modal.MapModal
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TextView

import android.graphics.Typeface

import android.view.Gravity

import android.widget.LinearLayout
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter

import com.google.android.gms.maps.model.Marker





class MapiFragment : Fragment(), OnMapReadyCallback {
    lateinit var dashBoardActivity: HomeDetailsActivity
    lateinit var sharprf: shareprefrences
    var map: GoogleMap? = null
    var markerList = ArrayList<MapModal>()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var currentLoaction: Location
    lateinit var latitude: String
    lateinit var longitute: String
    var REQUEST_CODE: Int = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mapi, container, false)
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
//        maplist= arrayListOf()
        dashBoardActivity = context as HomeDetailsActivity
        sharprf = shareprefrences(requireActivity())
        homescreendata()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)


    }





    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLoaction = location
                Toast.makeText(
                    context,
                    currentLoaction.latitude.toString() + "" + currentLoaction.longitude,
                    Toast.LENGTH_SHORT
                ).show()
                val supportMapFragment =
                    childFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment?
                supportMapFragment!!.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        Log.e("working", "working")
        map = googleMap
//        val latLng = LatLng(currentLoaction.latitude, currentLoaction.longitude)
//        latitude = currentLoaction.latitude.toString()
//        longitute = currentLoaction.longitude.toString()
//        if (latitude != null || longitute != null) {
//            //updatelocation()
//        }
////        for (i in 0 until maplist.size){
////            var lat=maplist[i].address.latitude
////            var lan=maplist[i].address.latitude
////            Log.e("latitude",lat + lan)
////        }
//        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
//        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
//        googleMap?.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }



    fun homescreendata() {
        dashBoardActivity.showProgressDialog()

        var orderdilivery: Call<HomeScreenReponse> = APIUtils.getServiceAPI()!!.homeScreen(
            sharprf.getStringPreference(dashBoardActivity.Token).toString(),
            sharprf.getStringPreference(dashBoardActivity.USER_ID).toString(),
        )
        orderdilivery.enqueue(object : Callback<HomeScreenReponse> {
            override fun onResponse(
                call: Call<HomeScreenReponse>,
                response: Response<HomeScreenReponse>
            ) {

                try {

                    if (response.code() == 200) {
                        dashBoardActivity.hideProgressDialog()

                        if (response.body()!!.status == "success") {
                            for (i in 0 until response!!.body()!!.data.hostArr.size) {
                                    var latitude: Double =
                                        response!!.body()!!.data.hostArr[i].latitude.toDouble()
                                    var longitude =
                                        response!!.body()!!.data.hostArr[i].longitude.toDouble()
                                    var title =  response!!.body()!!.data.hostArr[i].name
                                    markerList.add(MapModal(latitude, longitude, title))

                                    for (point in markerList) {
                                        val markerOptions = MarkerOptions().position(
                                            LatLng(
                                                point.latitutde,
                                                point.longitude
                                            )
                                        ).title(point.title).snippet(response!!.body()!!.data.hostArr[i].distance).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapdownload))

                                        map!!.setInfoWindowAdapter(object : InfoWindowAdapter {
                                            override fun getInfoWindow(arg0: Marker): View? {
                                                return null
                                            }

                                            override fun getInfoContents(marker: Marker): View {
                                                val info = LinearLayout(activity)
                                                info.orientation = LinearLayout.VERTICAL
                                                val title = TextView(activity)
                                                title.setTextColor(Color.BLACK)
                                                title.gravity = Gravity.CENTER
                                                title.setTypeface(null, Typeface.BOLD)
                                                title.text = marker.title
                                                val snippet = TextView(activity)
                                                snippet.setTextColor(Color.GRAY)
                                                snippet.text = marker.snippet
                                                info.addView(title)
                                                info.addView(snippet)
                                                return info
                                            }
                                        })

                                        map?.animateCamera(
                                            CameraUpdateFactory.newLatLng(
                                                LatLng(
                                                    point.latitutde,
                                                    point.longitude
                                                )
                                            )
                                        )
                                        map?.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(
                                                    point.latitutde,
                                                    point.longitude
                                                ), 5f
                                            )
                                        )
                                        map?.addMarker(markerOptions)
                                    }


                            }
                        } else {
                            dashBoardActivity.showToastMessage(activity, response.body()!!.message)
                        }

                    } else if (response.code() == 401) {
                        startActivity(Intent(activity, LoginActivity::class.java))
                        dashBoardActivity.finishAffinity()


                    } else if (response.code() == 400) {
                        dashBoardActivity.hideProgressDialog()
                        dashBoardActivity.showToastMessage(activity, response.body()!!.message)

                    }

                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<HomeScreenReponse>, t: Throwable) {
                dashBoardActivity.hideProgressDialog()
            }

        })
    }


}






