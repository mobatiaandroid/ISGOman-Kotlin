package com.example.isgoman_kotlin.activity.events.model

import com.example.isgoman_kotlin.activity.login.model.Response
import com.google.gson.annotations.SerializedName

class EventResponseModel (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: EventResponse
    )