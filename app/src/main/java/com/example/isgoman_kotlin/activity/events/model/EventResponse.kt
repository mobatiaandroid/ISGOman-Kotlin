package com.example.isgoman_kotlin.activity.events.model

import com.example.isgoman_kotlin.activity.login.model.StudentModels
import com.google.gson.annotations.SerializedName

class EventResponse (
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("about") val about: ArrayList<EventModels>,
    @SerializedName("banner_img") val banner_img: String
        )