package com.example.timezoneaware.di

import com.example.timezoneaware.concurrency.AppDispatchers
import com.example.timezoneaware.concurrency.IAppDispatchers
import com.example.timezoneaware.data.api.ApiClient
import com.example.timezoneaware.data.repository.auth.AuthRepository
import com.example.timezoneaware.data.repository.auth.IAuthRepository
import com.example.timezoneaware.data.repository.timezone.ITimeZoneRepositoryRepository
import com.example.timezoneaware.data.repository.timezone.TimeZoneRepository
import com.example.timezoneaware.data.repository.user.IUsersRepository
import com.example.timezoneaware.data.repository.user.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAppDispatchers(): IAppDispatchers {
        return AppDispatchers()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        apiClient: ApiClient,
        appDispatchers: AppDispatchers
    ): IAuthRepository = AuthRepository(apiClient, appDispatchers)

    @Singleton
    @Provides
    fun provideTimeZoneRepository(
        apiClient: ApiClient,
        appDispatchers: AppDispatchers
    ): ITimeZoneRepositoryRepository = TimeZoneRepository(apiClient, appDispatchers)

  @Singleton
    @Provides
    fun provideUsersRepository(
        apiClient: ApiClient,
        appDispatchers: AppDispatchers
    ): IUsersRepository = UsersRepository(apiClient, appDispatchers)

}
