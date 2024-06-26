package com.example.timezoneaware.data.repository.user

import com.example.timezoneaware.data.model.*
import com.example.timezoneaware.utils.DataResult

interface IUsersRepository {
    suspend fun getUserList(id:String): DataResult<UsersResponse?>

    suspend fun updateUserData(id:Int,userId:Int, user: InputUserData): DataResult<CustomResponse?>

    suspend fun deleteUserData(id:String, userId:Int): DataResult<CustomResponse?>
}