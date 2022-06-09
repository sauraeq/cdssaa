package com.eaxample.coffee.api


import com.equalinfotech.coffee.modal.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface APIConfiguration {


    @FormUrlEncoded
    @POST("signUp")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("mobileNumber") mobileNumber: String,
        @Field("signuptype") signuptype: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("fcmToken") fcmToken: String,
    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("forgotPassword")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun forgotPassword(
        @Field("email") email: String,
    ): Call<ForegetPasswordResponse>


    @FormUrlEncoded
    @POST("bookProduct")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun bookProduct(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("pId") pId: String,
        @Field("hostId") hostId: String,
        @Field("price") price: String,
    ): Call<BookingResponse>


    @FormUrlEncoded
    @POST("addRemoveQuantityCart")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun addRemoveQuantityCart(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("cart_id") hostId: String,
        @Field("type") price: String,
    ): Call<AddCartResponse>

    @FormUrlEncoded
    @POST("removeCartById")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun removeCartById(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("cart_id") cart_id: String,
    ): Call<CartDeleteResponse>


    @FormUrlEncoded
    @POST("login")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun login(
        @Field("email") email: String,
        @Field("signuptype") signuptype: String,
        @Field("password") password: String,
        @Field("g_id") g_id: String,
        @Field("fb_id") fb_id: String,
        @Field("name") name: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("fcmToken") fcmToken: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("resetPassword")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun resetPassword(
        @Field("consumerId") consumerId: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
        @Field("token") token: String,
    ): Call<OtpResponse>


    @FormUrlEncoded
    @POST("getCartItems")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun getCartItems(
        @Field("consumerId") consumerId: String,
        @Field("token") token: String,
    ): Call<CartResponse>


    @FormUrlEncoded
    @POST("emailVerification")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun emailVerification(
        @Field("consumerId") consumerId: String,
        @Field("token") token: String,
        @Field("otp") otp: String,
    ): Call<OtpResponse>


    @Multipart
    @POST("editProfileImage")
    fun updateProfileUser(
        @Part("consumerId") consumerId: RequestBody,
        @Part("token") token: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): Call<PictureResponse>

    @FormUrlEncoded
    @POST("cms_content")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun cms_content(
        @Field("consumerId") consumerId: String,
        @Field("token") token: String,
    ): Call<PrivacyResponse>


    @FormUrlEncoded
    @POST("get_profile")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun get_profile(
        @Field("consumerId") consumerId: String,
        @Field("token") token: String,
    ): Call<ProfileResponse>


    @FormUrlEncoded
    @POST("editProfile")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun editProfile(
        @Field("consumerId") consumerId: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobileNumber") mobileNumber: String,
        @Field("token") token: String,
    ): Call<ProfileDataUpdationReponse>


    @FormUrlEncoded
    @POST("getTermsConditionsLists")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun getTermsConditionsLists(
        @Field("token") token: String,
    ): Call<TermsConditionResponse>


    @FormUrlEncoded
    @POST("getFaqLists")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun getFaqLists(
        @Field("token") token: String,
    ): Call<FaqResponse>


    @FormUrlEncoded
    @POST("faqSearchList")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun faqSearchList(
        @Field("token") token: String,
        @Field("keyword") keyword: String,
    ): Call<FaqResponse>

    @FormUrlEncoded
    @POST("homeScreen")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun homeScreen(
        @Field("token") token: String,
        @Field("consumerId") keyword: String,
    ): Call<HomeScreenReponse>


    @FormUrlEncoded
    @POST(" ")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun hostDetail(
        @Field("token") token: String,
        @Field("consumerId") keyword: String,
        @Field("hostId") hostId: String,
    ): Call<CoffeedetailsResponse>

    @FormUrlEncoded
    @POST("productLists")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun productLists(
        @Field("token") token: String,
        @Field("consumerId") keyword: String,
        @Field("hostId") hostId: String,
    ): Call<ProductListResponse>


    @FormUrlEncoded
    @POST("cartItemCount")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun cartItemCount(
        @Field("token") token: String,
        @Field("consumerId") keyword: String,
    ): Call<CartCountResponse>


    @FormUrlEncoded
    @POST("scanQrCode")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun scanqrcode(
        @Field("token") token: String,
        @Field("consumerId") keyword: String,
        @Field("scan_data") scan_data: String,
    ): Call<ScanDataResponse>


    @FormUrlEncoded
    @POST("cartItemCount")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun scanreturn(
        @Field("token") token: String,
        @Field("consumerId") keyword: String,
        @Field("scan_data") scan_data: String,
    ): Call<ScanReturnResponse>


    @FormUrlEncoded
    @POST("getMembershipLists")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun getMembershipLists(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
    ): Call<MembershipResponse>


    @FormUrlEncoded
    @POST("checkout")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun checkout(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("cart_ids") cart_ids: String,
    ): Call<Checkoutresponse>


    @FormUrlEncoded
    @POST("inventory")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun inventory(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
    ): Call<InventoryResponse>


    @FormUrlEncoded
    @POST("membershippurchase_live")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun membershippurchase(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("mem_purchase_id") mem_purchase_id: String,
        @Field("subscriptionType") subscriptionType: String,
    ): Call<MembershipSubcrbtionResponse>


    @FormUrlEncoded
    @POST("getPaymentRequest_live")
    @Headers("ConTypetent-:application/x-www-form-urlencoded")
    fun getPaymentRequest(
        @Field("referenceId") referenceId: String,
    ): Call<PaymentstatusResponse>

    @FormUrlEncoded
    @POST("getNotification")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun getNotification(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
    ): Call<NotificationResponse>


    @FormUrlEncoded
    @POST("orderHistory")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun orderHistory(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("monthYear") monthYear: String,
    ): Call<OrderHistoryResponse>

    @FormUrlEncoded
    @POST("returnProduct")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun returnProduct(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("hostId") hostId: String,
        @Field("returnProduct") returnProduct: JSONArray,
    ): Call<ReturndataResponse>


    @FormUrlEncoded
    @POST("updateLatLong")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun updateLatLong(
        @Field("token") token: String,
        @Field("consumerId") consumerId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
    ): Call<LocationUpdateResponse>

//    @POST("returnProduct")
//    @Headers("Content-Type: application/json")
//    fun returnProduct(
//        @Field("token") token:String,
//        @Field("consumerId") consumerId:String,
//        @Field("hostId") hostId:String,
//        @Body stringHashMap: Requestdata
//    ): Call<ReturndataResponse>

    @POST("paymentgateway_sandbox.php")
    @Headers("Content-Type:application/x-www-form-urlencoded")
    fun swishpayment(
        @Body stringStringHashMap: HashMap<String, String>
    ): Call<DataResponse>


}