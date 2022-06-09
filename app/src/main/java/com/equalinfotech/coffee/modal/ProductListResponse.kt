package com.equalinfotech.coffee.modal

data class ProductListResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val hostProduct: List<HostProduct>
    ) {
        data class HostProduct(
            val createdAt: String,
            val description: String,
            val image: String,
            val name: String,
            val pId: String,
            val price: String,
            val reusable_items: String
        )
    }
}