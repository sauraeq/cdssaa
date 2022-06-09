package com.equalinfotech.coffee.modal

data class NotificationResponse(
    val `data`: List<Data>,
    val message: String,
    val status: String
) {
    data class Data(
        val consumerId: String,
        val createdAt: String,
        val id: String,
        val message: String,
        val notification_read: String,
        val title: String,
        val type: String,
        val value: String
    )
}