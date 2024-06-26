package com.example.timezoneaware.data.repository.auth

import com.example.timezoneaware.data.model.AuthResponse
import com.example.timezoneaware.data.model.UserAuth
import com.example.timezoneaware.utils.DataResult

interface IAuthRepository {
    suspend fun login(userAuth: UserAuth): DataResult<AuthResponse?>
    suspend fun register(userAuth: UserAuth): DataResult<AuthResponse?>
}