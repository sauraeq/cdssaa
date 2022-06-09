package com.equalinfotech.coffee.modal

data class ScanReturnResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val hostId: String,
        val productReturnList: List<ProductReturn>,
        val scan_type: String
    ) {
        data class ProductReturn(
            val cart_id: String,
            val image: String,
            val name: String,
            val pId: String,
            val quantity: String
        )
    }
}