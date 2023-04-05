package com.example.isgoman_kotlin.activity.login.model

import com.google.gson.annotations.SerializedName

class Response (
    @SerializedName("response") val response: String,
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("parentId") val parentId: String,
    @SerializedName("students") val data: ArrayList<StudentModels>,
    @SerializedName("badge_count") val badge_count: String

)