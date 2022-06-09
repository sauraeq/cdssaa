package com.equalinfotech.coffee.modal

data class FaqResponse(
    val faqs: List<Faq>,
    val message: String,
    val status: String
) {
    data class Faq(
        val faq_answer: String,
        val faq_id: String,
        val faq_question: String
    )
}