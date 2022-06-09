package com.equalinfotech.coffee.modal

data class MembershipSubcrbtionResponse(
    val `data`: List<Any>,
    val message: String,
    val status: String,
    val token: String,
    val id: String,
    val amount: String,
    val callbackUrl: String,
    val payment_url: String,
    val subscriptionType: String,
)