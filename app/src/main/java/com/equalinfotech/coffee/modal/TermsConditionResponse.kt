package com.equalinfotech.coffee.modal

data class TermsConditionResponse(
    val message: String,
    val status: String,
    val tc_list: List<Tc>
) {
    data class Tc(
        val tc_content: String,
        val tc_id: String
    )
}