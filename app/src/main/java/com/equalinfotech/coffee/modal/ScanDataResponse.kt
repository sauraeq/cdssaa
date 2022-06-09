package com.equalinfotech.coffee.modal

data class ScanDataResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val hostId: String,
        val hostProduct: List<HostProduct>,
        val productReturnList: List<ProductReturn>
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

        data class ProductReturn(
            val cart_id: String,
            val image: String,
            val hostname:String,
            val name: String,
            val pId: String,
            var quantity: Int,
            var category_name: String,
            var finalquantity: Int = 0
        )
    }
}