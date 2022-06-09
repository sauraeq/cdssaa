package com.equalinfotech.coffee.modal

data class InventoryResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val total_quantity:String,
        val inventory: List<Inventory>
    ) {
        data class Inventory(
            val name: String,
            val orderId: String,
            val ordered_date: String,
            val pId: String,
            val quantity: String,
            val return_day_left: String,
            val category_name: String,
            val reusable_items: String
        )
    }
}