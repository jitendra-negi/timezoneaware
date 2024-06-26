package com.example.timezoneaware.data.model

import com.google.gson.annotations.SerializedName

data class CustomResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("data")
    val data: String,
    @SerializedName("success")
    val success: Boolean
)