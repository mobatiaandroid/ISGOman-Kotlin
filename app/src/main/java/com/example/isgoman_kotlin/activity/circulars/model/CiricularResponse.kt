package com.example.isgoman_kotlin.activity.circulars.model

import com.example.isgoman_kotlin.activity.events.model.EventModels
import com.google.gson.annotations.SerializedName

class CiricularResponse (
    @SerializedName("statuscode") val statuscode: String,
    @SerializedName("specialExams") val specialExams: ArrayList<CircularModel>

        )