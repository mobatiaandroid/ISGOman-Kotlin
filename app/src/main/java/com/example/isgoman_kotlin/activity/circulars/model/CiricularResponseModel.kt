package com.example.isgoman_kotlin.activity.circulars.model

import com.example.isgoman_kotlin.activity.events.model.EventResponse
import com.google.gson.annotations.SerializedName

class CiricularResponseModel (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: CiricularResponse
        )