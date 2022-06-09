package com.equalinfotech.coffee.modal

data class CartCountResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val cart_quantity: String
    )
}