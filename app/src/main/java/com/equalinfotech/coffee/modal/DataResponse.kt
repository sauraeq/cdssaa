package com.equalinfotech.coffee.modal

data class DataResponse(
    val error: String,
    val id: String,
    val msg: String,
    val payment_url: String,
    val success: String,
    val token: String,
    val callbackUrl: String
)