package com.equalinfotech.coffee.modal

data class ScanOrderResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val hostId: String,
        val hostProduct: List<HostProduct>,
        val scan_type: String
    ) {
        data class HostProduct(
            val createdAt: String,
            val description: String,
            val hostId: String,
            val image: String,
            val name: String,
            val pId: String,
            val price: String,
            val reusable_items: String
        )
    }
}