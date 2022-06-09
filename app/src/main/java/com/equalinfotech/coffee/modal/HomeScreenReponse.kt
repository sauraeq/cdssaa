package com.equalinfotech.coffee.modal

data class HomeScreenReponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val hostArr: List<HostArr>,
        val slider_list: List<Slider>
    ) {
        data class HostArr(
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

        data class Slider(
            val image: String,
            val slider_id: String
        )
    }
}