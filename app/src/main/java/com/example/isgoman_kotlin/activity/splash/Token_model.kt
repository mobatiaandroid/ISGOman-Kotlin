package com.example.isgoman_kotlin.activity.splash

import com.google.gson.annotations.SerializedName

class Token_model (
    @SerializedName("access_token") val access_token: String,
    @SerializedName("expires_in") val expires_in: Int,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("scope") val scope: Int,
    @SerializedName("refresh_token") val refresh_token: String
        )