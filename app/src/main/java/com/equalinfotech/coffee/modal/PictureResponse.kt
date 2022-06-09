package com.equalinfotech.coffee.modal

data class PictureResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val id: String,
        val profileImage: String
    )
}