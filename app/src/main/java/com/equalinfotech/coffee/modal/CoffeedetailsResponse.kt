package com.equalinfotech.coffee.modal

data class CoffeedetailsResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val hostData: HostData,
        val hostOffer: List<Any>
    ) {
        data class HostData(
            val address: String,
            val city: String,
            val description: String,
            val distance: String,
            val hostId: String,
            val latitude: String,
            val longitude: String,
            val name: String,
            val pinCode: String,
            val profileImage: String
        )
    }
}