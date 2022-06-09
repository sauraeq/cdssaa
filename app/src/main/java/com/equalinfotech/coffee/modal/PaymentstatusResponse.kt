package com.equalinfotech.coffee.modal

data class PaymentstatusResponse(
    val callbackUrl: String,
    val error: String,
    val id: String,
    val msg: String,
    val payeeAlias: String,
    val payeePaymentReference: String,
    val payerAlias: Any,
    val paymentReference: String,
    val status: String,
    val success: String
)