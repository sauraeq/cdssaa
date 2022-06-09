package com.equalinfotech.coffee.modal

data class ProfileResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val accountStatus: String,
        val agreed_tc_ids: String,
        val createdAt: String,
        val email: String,
        val emailVerification: String,
        val fb_id: String,
        val fcmToken: String,
        val g_id: String,
        val id: String,
        val isLogin: String,
        val latitude: String,
        val longitude: String,
        val mobileNumber: String,
        val name: String,
        val password: String,
        val profileImage: String,
        val signuptype: String,
        val token: String
    )
}