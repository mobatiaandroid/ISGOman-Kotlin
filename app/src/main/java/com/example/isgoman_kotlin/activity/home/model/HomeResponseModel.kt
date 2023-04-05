package com.example.isgoman_kotlin.activity.home.model

import com.example.isgoman_kotlin.activity.login.model.Response
import com.google.gson.annotations.SerializedName

class HomeResponseModel (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: HomeResponse
        )