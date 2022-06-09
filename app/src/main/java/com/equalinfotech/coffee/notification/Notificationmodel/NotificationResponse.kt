package com.equalinfotech.learnorteach.notification.Notificationmodel

data class NotificationResponse(
    val `data`: List<Data>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
) {
    data class Data(
        val category_name: String,
        val created_date: String,
        val first_name: String,
        val last_name: String,
        val topic_name: String,
        val user_image: String
    )
}