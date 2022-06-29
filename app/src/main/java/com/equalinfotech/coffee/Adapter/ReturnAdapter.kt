package com.equalinfotech.coffee.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.equalinfotech.coffee.Activity.OrderReturnActivity
import com.equalinfotech.coffee.Activity.OrderSucessfullActivity
import com.equalinfotech.coffee.R
import com.equalinfotech.coffee.api.APIUtils
import com.equalinfotech.coffee.modal.ReturnByIdResponse
import com.equalinfotech.coffee.modal.ReturndataResponse
import com.equalinfotech.coffee.modal.ScanDataResponse
import kotlinx.android.synthetic.main.adapter_return.view.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReturnAdapter(var context: Context,var returnlist:ArrayList<ScanDataResponse.Data.ProductReturn>,var user_id:String, var Token:String,var host_id:String):RecyclerView.Adapter<ReturnAdapter.ReturnHolder>() {
   inner class ReturnHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       var cart_id:String=""
       init {
           itemView.minius.setOnClickListener {

               if((context as OrderReturnActivity).returndata[position].quantity!=1){
                   addsub(adapterPosition,itemView.quanty,"sub")
               }
           }
           itemView.add.setOnClickListener {
               addsub(adapterPosition,itemView.quanty,"add")

           }
           itemView.return_image.setOnClickListener {
               notifyDataSetChanged()
               cart_id=(context as OrderReturnActivity).returndata[position].cart_id
               returnbyid(cart_id)

           }
       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_return, parent, false)
        return ReturnHolder(view)
    }

    override fun onBindViewHolder(holder: ReturnHolder, position: Int) {
        if(returnlist[position].category_name=="Pizza Boxes"){
            holder.itemView.imageeee.setImageResource(R.drawable.pizzabox)
        }else{
            holder.itemView.imageeee.setImageResource(R.drawable.cupicon)
        }
        returnlist[position].finalquantity=returnlist[position].quantity
            holder.itemView.nameofproduct.text=returnlist[position].name
            holder.itemView.quanty.text=returnlist[position].quantity.toString()

    }

    override fun getItemCount(): Int {
       return returnlist.size
    }

    fun addsub(position: Int,textView: TextView,type:String){
        if(type=="add"){
            if(returnlist[position].quantity<returnlist[position].finalquantity){
                returnlist[position].quantity=returnlist[position].quantity+1
            }else{
                Toast.makeText(context,"Your Can not apply more than purchases quantity",Toast.LENGTH_SHORT).show()
            }

        }else{
            if(returnlist[position].quantity!=1){
                returnlist[position].quantity=returnlist[position].quantity-1
            }

        }
        (context as OrderReturnActivity).returndata[position].quantity=returnlist[position].quantity
        textView.text==returnlist[position].quantity.toString()
    }

    fun returnbyid(cartid:String) {

            (context as OrderReturnActivity).showProgressDialog()
            var orderdilivery: Call<ReturnByIdResponse> =APIUtils.getServiceAPI()!!.returnsingleProduct(Token,user_id,host_id,cartid,"1")
            orderdilivery.enqueue(object : Callback<ReturnByIdResponse> {
                override fun onResponse(
                    call: Call<ReturnByIdResponse>,
                    response: Response<ReturnByIdResponse>
                ) {

                    try {

                        if (response.code() == 200) {
                            (context as OrderReturnActivity).hideProgressDialog()
                            if (response.body()!!.status == "success") {
                                (context as OrderReturnActivity).showToastMessage(
                                    context,
                                    response.body()!!.message
                                )

                                context.startActivity(Intent(context, OrderSucessfullActivity::class.java))

                            } else {
                                (context as OrderReturnActivity).showToastMessage(
                                    context,
                                    response.body()!!.message
                                )
                            }

                        } else if (response.code() == 401) {


                            (context as OrderReturnActivity).finishAffinity()


                        } else if (response.code() == 400) {
                            (context as OrderReturnActivity).hideProgressDialog()
                            (context as OrderReturnActivity).showToastMessage(
                                context,
                                response.body()!!.message
                            )

                        }

                    } catch (e: Exception) {
                        (context as OrderReturnActivity).showToastMessage(
                            context,
                            e.toString()
                        )
                    }

                }

                override fun onFailure(call: Call<ReturnByIdResponse>, t: Throwable) {
                    (context as OrderReturnActivity).hideProgressDialog()
                }

            })
        }

    }
