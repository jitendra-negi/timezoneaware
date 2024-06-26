package com.example.timezoneaware.data.repository.user

import com.example.timezoneaware.concurrency.AppDispatchers
import com.example.timezoneaware.data.api.ApiClient
import com.example.timezoneaware.data.model.CustomResponse
import com.example.timezoneaware.data.model.InputUserData
import com.example.timezoneaware.data.model.UsersResponse
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.safeDataResult


class UsersRepository (
    private val apiClient: ApiClient,
    private val appDispatchers: AppDispatchers
) :IUsersRepository {

    override suspend fun getUserList(id:String): DataResult<UsersResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.getUserList(id) }

    override suspend fun updateUserData(id: Int,userId:Int, user: InputUserData): DataResult<CustomResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.updateUsers(id,userId,user ) }

    override suspend fun deleteUserData(id:String, userId:Int): DataResult<CustomResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.deleteUser(id,userId ) }

}