package com.example.isgoman_kotlin.constants.api


import android.content.Context
import com.example.isgoman_kotlin.activity.aboutus.model.AboutResponseModel
import com.example.isgoman_kotlin.activity.circulars.model.CiricularResponseModel
import com.example.isgoman_kotlin.activity.events.model.EventResponseModel
import com.example.isgoman_kotlin.activity.home.model.HomeResponseModel
import com.example.isgoman_kotlin.activity.login.model.Login_model
import com.example.isgoman_kotlin.activity.splash.Token_model
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * Created by Arshad on 09,August,2022
 */
interface ApiService {
    val context: Context
    /*************access token****************/
    @POST("oauth/access_token")
    @FormUrlEncoded
    fun token(
        @Field("type") type: Int,
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<Token_model>



    @POST("api/login")
    @FormUrlEncoded
    fun login_2(
        @Field("type") type: Int,
        @Field("access_token") access_token: String,
        @Field("phone") phone: String,
        @Field("passwd") password: String,
        @Field("device_type") device_type: String,
        @Field("device_id") device_id: String,
        ): Call<Login_model>

    @POST("api/parentRegistration")
    @FormUrlEncoded
    fun signup(
        @Field("type") type: Int,
        @Field("access_token") access_token: String,
        @Field("parent_name") parent_name: String,
        @Field("parent_email") parent_email: String,
        @Field("parent_phone") parent_phone: String,
        @Field("student_gr_no") student_gr_no: String,
        @Field("device_type") device_type: String,
        @Field("device_id") device_id: String,
    ): Call<ResponseBody>

    @POST("api/homeSlider")
    @FormUrlEncoded
    fun homeslider(
        @Field("type") type: Int,
        @Field("access_token") access_token: String
    ): Call<HomeResponseModel>

    @POST("api/events")
    @FormUrlEncoded
    fun events(
        @Field("type") type: Int,
        @Field("access_token") access_token: String,
        @Field("boardId") boardId: String,
    @Field("studentId") studentId: String,
    @Field("parentId") parentId: String,
    ): Call<EventResponseModel>



    @POST("api/eventRegistration")
    @FormUrlEncoded
    fun eventsreg(
        @Field("type") type: Int,
        @Field("access_token") access_token: String,
        @Field("eventId") eventId: String,
        @Field("studentId") studentId: String,
        @Field("parentId") parentId: String,
    ): Call<EventResponseModel>



    @POST("api/circulars")
    @FormUrlEncoded
    fun ciricular(
        @Field("access_token") access_token: String,
        @Field("boardId") boardId: String,
        @Field("studentId") studentId: String,
        @Field("parentId") parentId: String,
    ): Call<CiricularResponseModel>


    @POST("api/aboutUs")
    @FormUrlEncoded
    fun aboutus(
        @Field("access_token") access_token: String,
        @Field("boardId") boardId: String
    ): Call<AboutResponseModel>
}
