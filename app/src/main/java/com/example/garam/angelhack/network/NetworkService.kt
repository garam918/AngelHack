package com.example.garam.angelhack.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.Callback

interface NetworkService {

    @POST("/v1/payment/ready")
    @Headers("Content_Type: application/x-www-form-urlencoded;charset=utf-8")
    @FormUrlEncoded
    fun payments(
        @Header("Authorization") key: String,
        @Field ("cid") cid: String,
        @Field ("partner_order_id") partner_order_id: String,
        @Field ("partner_user_id") partner_user_id: String,
        @Field ("item_name") item_name: String,
        @Field ("quantity") quantity: Int,
        @Field ("total_amount") total_amount: Int,
        @Field ("tax_free_amount") tax_free_amount: Int,
        @Field ("approval_url") approval_url: String,
        @Field ("fail_url") fail_url: String,
        @Field ("cancel_url") cancel_url: String
        ): Call<JsonObject>

    @POST("/v1/payment/order")
    @Headers("Content_Type: application/x-www-form-urlencoded;charset=utf-8")
    @FormUrlEncoded
    fun payCheck(
        @Header("Authorization") key: String,
        @Field ("cid") cid : String,
        @Field ("tid") tid : String
    ): Call<JsonObject>


    @POST("/user_check")
    fun userlogin(
        @Body info : JsonObject
    ) : Call<JsonObject>

    @POST("/usermode/qrscan")
    fun prePayment(
        @Body payInfo : JsonObject
    ) : Call<JsonObject>

    @POST("/usermode/payment_save")
    fun paymentSave(
        @Body paymentInfo : JsonObject
    ) : Call<JsonObject>

    @Multipart
    @POST("/hostmode/mypage/save")
    fun qrSend(
        @Part("json") json: RequestBody,
        @Part qrfile: MultipartBody.Part?
    ): Call<String>

    @POST("/usermode/subtract_request")
    fun pointSend(
        @Body pointAmount : JsonObject
    ): Call<JsonObject>

    @POST("/hostmode/charge_point")
    fun chargePoint(
        @Body hid: JsonObject
    ): Call<JsonObject>

    @POST("/host_check")
    fun hostcheck(
        @Body hid: JsonObject
    ): Call<JsonObject>

    @POST("/hostmode/creditlist")
    fun payList(
        @Body hid: JsonObject
    ): Call<JsonArray>

}