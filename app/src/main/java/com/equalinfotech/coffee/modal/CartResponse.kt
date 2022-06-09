package com.equalinfotech.coffee.modal

data class CartResponse(
    val cartProductList: List<CartProduct>,
    val message: String,
    val offer: String,
    val status: String,
    val sub_total: Int,
    val total: Int
) {
    data class CartProduct(
        val cart_id: String,
        val description: String,
        val image: String,
        val name: String,
        val pId: String,
        val price: String,
        var quantity: Int,
    )
}