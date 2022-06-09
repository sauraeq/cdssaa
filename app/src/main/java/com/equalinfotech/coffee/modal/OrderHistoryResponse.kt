package com.equalinfotech.coffee.modal

data class OrderHistoryResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val coffee_ord_count: Int,
        val order_data: List<OrderData>,
        val pizza_ord_count: Int
    ) {
        data class OrderData(
            val host_name: String,
            val image: String,
            val name: String,
            val orderId: String,
            val ordered_date: String
        )
    }
}