package com.example.timezoneaware.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AuthResponse(
    @SerializedName("data")
    val data: User,
    @SerializedName("success")
    val success: Boolean
): Parcelable


@Parcelize
data class User(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userPassword")
    val userPassword: String,
    @SerializedName("userType")
    val userType: Int? = 0
): Parcelable

data class InputUserData(
    val userName: String,
)


data class UserAuth(
    val userName: String,
    val userPassword: String,
)


@Parcelize
data class UsersResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("data")
    val data: ArrayList<User>,
    @SerializedName("success")
    val success: Boolean
): Parcelable
