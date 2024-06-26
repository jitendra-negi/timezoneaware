package com.example.timezoneaware.data.api

import com.example.timezoneaware.data.model.*
import retrofit2.http.*

interface ApiClient {

    @POST("timezone/account/login")
    suspend fun login(
        @Body user: UserAuth
    ): AuthResponse

    @POST("timezone/account/register")
    suspend fun register(
        @Body user: UserAuth
    ): AuthResponse

    @POST("timezone/add")
    suspend fun addTimezone(
        @Body time: InputTimezone
    ): TimeZoneResponse

    @GET("timezones/user/{id}")
    suspend fun getTimeZoneList(
        @Path("id") id: String
    ): TimeZonesResponse

    @GET("timezones/user/{id}")
    suspend fun getTimeZoneListByID(
        @Path("id") id: String
    ): TimeZonesResponse


    @DELETE("timezone/timezone_delete/{id}")
    suspend fun deleteCityTimeZone(
        @Path("id") id: String
    ): TimeZoneResponse

    @PUT("timezone/timezone_update/{id}")
    suspend fun updateCityTimeZone(
        @Path("id") id: String,
        @Body time: InputTimezone
    ): TimeZoneResponse

    @GET("timezone/timezone_search/{id}/{city}")
    suspend fun searchCityList(
        @Path("id") id: String,
        @Path("city") city: String
    ): TimeZonesResponse

    /*User management*/

    @GET("timezone/account/users/{id}")
    suspend fun getUserList(
        @Path("id") id: String
    ): UsersResponse

    @DELETE("timezone/account/user_delete/{id}/{userId}")
    suspend fun deleteUser(
        @Path("id") id: String,  @Path("userId") userId: Int
    ): CustomResponse

    @PUT("timezone/account/user_update/{id}/{userId}")
    suspend fun updateUsers(
        @Path("id") id: Int,
        @Path("userId") userId: Int,
        @Body user: InputUserData
    ): CustomResponse
}