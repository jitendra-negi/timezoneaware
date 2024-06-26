package com.example.timezoneaware.data.repository.timezone

import com.example.timezoneaware.concurrency.AppDispatchers
import com.example.timezoneaware.data.api.ApiClient
import com.example.timezoneaware.data.model.InputTimezone
import com.example.timezoneaware.data.model.TimeZoneResponse
import com.example.timezoneaware.data.model.TimeZonesResponse
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.safeDataResult

class TimeZoneRepository(
    private val apiClient: ApiClient,
    private val appDispatchers: AppDispatchers
) : ITimeZoneRepositoryRepository {

    override suspend fun addTimezone(objTimezone: InputTimezone): DataResult<TimeZoneResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.addTimezone(objTimezone) }

    override suspend fun getTimezoneListData(id:String): DataResult<TimeZonesResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.getTimeZoneList(id) }

    override suspend fun deleteCityTimezone(id: String): DataResult<TimeZoneResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.deleteCityTimeZone(id) }

    override suspend fun updateCityTimezone(id: String,objTimezone: InputTimezone): DataResult<TimeZoneResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.updateCityTimeZone(id, objTimezone) }

    override suspend fun searchCityTimezone(id:String,city:String): DataResult<TimeZonesResponse?> =
        safeDataResult(appDispatchers.io) { apiClient.searchCityList(id,city) }

}