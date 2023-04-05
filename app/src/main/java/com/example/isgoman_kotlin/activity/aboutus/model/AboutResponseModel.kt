package com.example.isgoman_kotlin.activity.aboutus.model

import com.example.isgoman_kotlin.activity.circulars.model.CiricularResponse
import com.google.gson.annotations.SerializedName

class AboutResponseModel (
        @SerializedName("responsecode") val responsecode: String,
        @SerializedName("response") val response: AboutResponse
        )