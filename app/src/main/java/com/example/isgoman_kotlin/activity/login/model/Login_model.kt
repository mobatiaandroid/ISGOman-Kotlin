package com.example.isgoman_kotlin.activity.login.model

import com.google.gson.annotations.SerializedName

class Login_model (
    @SerializedName("responsecode") val responsecode: String,
    @SerializedName("response") val response: Response
        )