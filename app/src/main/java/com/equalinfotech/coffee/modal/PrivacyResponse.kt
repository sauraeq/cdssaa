package com.equalinfotech.coffee.modal

data class PrivacyResponse(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val cms_content: String,
        val cms_id: String,
        val cms_page_name: String
    )
}