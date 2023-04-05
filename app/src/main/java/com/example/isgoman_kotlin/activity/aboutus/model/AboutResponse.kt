package com.example.isgoman_kotlin.activity.aboutus.model

import com.example.isgoman_kotlin.activity.circulars.model.CircularModel
import com.google.gson.annotations.SerializedName

class AboutResponse (
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("about") val about: ArrayList<AboutUsModel>,
    @SerializedName("banner_img") val banner_img:String
        )