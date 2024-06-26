package com.example.timezoneaware.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TimeZonesResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("data")
    val data: List<TimeZoneInfo>,
    @SerializedName("success")
    val success: Boolean
): Parcelable

data class TimeZoneResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("data")
    val data: String,
    @SerializedName("success")
    val success: Boolean
)

@Parcelize
data class TimeZoneInfo(

    @SerializedName("timeZoneId")
    val timeZoneId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("timeZoneCityName")
    val cityName: String,
    @SerializedName("timeZoneCityNameTimeZone")
    val timeZoneCityName: String,
    @SerializedName("timeZoneCityGMTDifference")
    val gmtDifference: String
): Parcelable

data class InputTimezone(
    val timeZoneCityName: String,
    val timeZoneCityNameTimeZone: String,
    val timeZoneCityGMTDifference: String,
    val userId: String,
)