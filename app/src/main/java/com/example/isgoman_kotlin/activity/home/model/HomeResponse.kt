package com.example.isgoman_kotlin.activity.home.model

import com.example.isgoman_kotlin.activity.login.model.StudentModels
import com.google.gson.annotations.SerializedName

class HomeResponse (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("android_version") val android_version: String,
    @SerializedName("ios_version") val ios_version: String,
    @SerializedName("sliders") val sliders: ArrayList<BannerList>,


)