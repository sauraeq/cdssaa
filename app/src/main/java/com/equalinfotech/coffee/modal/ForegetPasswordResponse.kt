package com.equalinfotech.coffee.modal

data class ForegetPasswordResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val consumerId: String,
        val otp: Int,
        val token: String
    )
}