package com.equalinfotech.coffee.modal

class Requestdata : ArrayList<Requestdata.RequestdataItem>(){
    data class RequestdataItem(
        val cart_id: String,
        val quantity: String
    )
}