package com.equalinfotech.coffee.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.equalinfotech.coffee.Activity.CartActivity
import com.equalinfotech.coffee.Activity.LoginActivity
import com.equalinfotech.coffee.Activity.OrderReturnActivity
import com.equalinfotech.coffee.Adapter.OrderAdapter
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.CartCountResponse
import com.equalinfotech.learnorteach.localsaved.shareprefrences
import kotlinx.android.synthetic.main.fragment_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderFragment : Fragment() {

    lateinit var shrpre: shareprefrences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shrpre = shareprefrences(requireActivity())
        count()
        orderrecycle.layoutManager = LinearLayoutManager(activity)

        btn_order.setOnClickListener {
//            if ((context as OrderReturnActivity).cartqun == "0") {
//                Toast.makeText(requireContext(), "Please Select Product", Toast.LENGTH_LONG).show()
//            } else {
                startActivity(Intent(activity, CartActivity::class.java))
//            }

        }
        if ((context as OrderReturnActivity).bookingitem.size > 0) {
            errrortest.visibility = View.GONE
        } else {
            errrortest.visibility = View.VISIBLE
        }
        orderrecycle.adapter = OrderAdapter(
            requireActivity(),
            (context as OrderReturnActivity).bookingitem,
            (context as OrderReturnActivity).hostid
        )
    }


    fun count() {
        (context as OrderReturnActivity).showProgressDialog()
        var orderdilivery: Call<CartCountResponse> = APIUtils.getServiceAPI()!!.cartItemCount(
            shrpre.getStringPreference("token").toString(),
            shrpre.getStringPreference("customer_id").toString(),
        )
        orderdilivery.enqueue(object : Callback<CartCountResponse> {
            override fun onResponse(
                call: Call<CartCountResponse>,
                response: Response<CartCountResponse>
            ) {
                try {
                    if (response.code() == 200) {
                        (context as OrderReturnActivity).hideProgressDialog()
                        if (response.body()!!.status == "success") {
                            (context as OrderReturnActivity).cartqun = response.body()!!.data.cart_quantity
                        } else {

                        }
                    } else if (response.code() == 401) {
                        (context as OrderReturnActivity).hideProgressDialog()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        (context as OrderReturnActivity).finishAffinity()
                    } else if (response.code() == 400) {
                        (context as OrderReturnActivity).hideProgressDialog()
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(call: Call<CartCountResponse>, t: Throwable) {
                (context as OrderReturnActivity).hideProgressDialog()
            }

        })
    }


}