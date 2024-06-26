package com.example.timezoneaware.data.repository.auth

import com.example.timezoneaware.concurrency.IAppDispatchers
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.safeDataResult
import com.example.timezoneaware.data.api.ApiClient
import com.example.timezoneaware.data.model.AuthResponse
import com.example.timezoneaware.data.model.UserAuth

class AuthRepository(
    private val apiClient: ApiClient,
    private val appDispatchers: IAppDispatchers
) : IAuthRepository {

    override suspend fun login(userAuth: UserAuth): DataResult<AuthResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.login(userAuth) }

    override suspend fun register(userAuth: UserAuth): DataResult<AuthResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.register(userAuth) }

}