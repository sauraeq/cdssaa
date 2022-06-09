package com.equalinfotech.coffee.modal

data class MembershipResponse(
    val memb_list_for_member: List<MembForMember>,
    val memb_list_for_new: List<Any>,
    val message: String,
    val status: String,
    val memb_purchase_status:String,
    val allow_purchase:String
) {
    data class MembForMember(
        val currency: String,
        val duration: String,
        val is_member: String,
        var datakey:Int=0,
        var containers_to_loan_per_use:Int,
        var total_containers_to_loan:Int,
        var days_to_return_containers:Int,
        val membership_description: String,
        val membership_id: String,
        val membership_name: String,
        val membership_price: String,
        val reusable_items: String
    )
}