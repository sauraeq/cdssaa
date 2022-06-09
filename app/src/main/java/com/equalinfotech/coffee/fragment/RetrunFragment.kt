package com.equalinfotech.coffee.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import com.equalinfotech.coffee.Activity.OrderReturnActivity
import com.equalinfotech.coffee.Activity.OrderSucessfullActivity
import com.equalinfotech.coffee.Adapter.ReturnAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.Requestdata
import com.equalinfotech.coffee.modal.ReturndataResponse
import kotlinx.android.synthetic.main.fragment_retrun.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrunFragment : Fragment() {
    lateinit var sharprf: shareprefrences
    var list: ArrayList<Requestdata> = arrayListOf<Requestdata>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_retrun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("returndata", (context as OrderReturnActivity).returndata.toString())
        sharprf = shareprefrences(requireActivity())
        list = arrayListOf<Requestdata>()
        if ((context as OrderReturnActivity).returndata.size > 0) {
            errrortest.visibility = View.GONE
            btn_return.visibility = View.VISIBLE
        } else {
            errrortest.visibility = View.VISIBLE
            btn_return.visibility = View.GONE
        }
        return_recycle.layoutManager = LinearLayoutManager(activity)
        return_recycle.adapter =
            ReturnAdapter(requireActivity(), (context as OrderReturnActivity).returndata)
        btn_return.setOnClickListener {
            returndata()
        }

    }

    fun init() {

        var jsonArray = JSONArray()
        for (i in 0 until (context as OrderReturnActivity).returndata.size) {
            var jsondata = JSONObject()
            jsondata.put("cart_id", (context as OrderReturnActivity).returndata[i].cart_id)
            jsondata.put(
                "quantity",
                (context as OrderReturnActivity).returndata[i].quantity.toString()
            )
            jsonArray.put(jsondata)
        }
    }


    fun returndata() {
        var jsonArray = JSONArray()
        for (i in 0 until (context as OrderReturnActivity).returndata.size) {
            var jsondata = JSONObject()
            jsondata.put("cart_id", (context as OrderReturnActivity).returndata[i].cart_id)
            jsondata.put(
                "quantity",
                (context as OrderReturnActivity).returndata[i].quantity.toString()
            )
            jsonArray.put(jsondata)
            Log.e("jasosnsn",jsonArray.toString())

//for(i in 0 until (context as OrderReturnActivity).returndata.size){
//    testdata.add(Requestdata.RequestdataItem((context as OrderReturnActivity).returndata[i].cart_id,(context as OrderReturnActivity).returndata[i].quantity.toString()))
//}
//list=Requestdata(testdata)
            (context as OrderReturnActivity).showProgressDialog()
            var orderdilivery: Call<ReturndataResponse> =
                APIUtils.getServiceAPI()!!.returnProduct(
                    sharprf.getStringPreference((context as OrderReturnActivity).Token).toString(),
                    sharprf.getStringPreference((context as OrderReturnActivity).USER_ID)
                        .toString(),
                    (context as OrderReturnActivity).hostid,
                    jsonArray
                )
            orderdilivery.enqueue(object : Callback<ReturndataResponse> {
                override fun onResponse(
                    call: Call<ReturndataResponse>,
                    response: Response<ReturndataResponse>
                ) {

                    try {

                        if (response.code() == 200) {
                            (context as OrderReturnActivity).hideProgressDialog()
                            if (response.body()!!.status == "success") {
                                    (context as OrderReturnActivity).showToastMessage(
                                activity,
                                response.body()!!.message
                            )

                                startActivity(Intent(activity, OrderSucessfullActivity::class.java))

                            } else {
                                (context as OrderReturnActivity).showToastMessage(
                                    activity,
                                    response.body()!!.message
                                )
                            }

                        } else if (response.code() == 401) {


                            (context as OrderReturnActivity).finishAffinity()


                        } else if (response.code() == 400) {
                            (context as OrderReturnActivity).hideProgressDialog()
                            (context as OrderReturnActivity).showToastMessage(
                                activity,
                                response.body()!!.message
                            )

                        }

                    } catch (e: Exception) {

                    }

                }

                override fun onFailure(call: Call<ReturndataResponse>, t: Throwable) {
                    (context as OrderReturnActivity).hideProgressDialog()
                }

            })
        }

    }
}