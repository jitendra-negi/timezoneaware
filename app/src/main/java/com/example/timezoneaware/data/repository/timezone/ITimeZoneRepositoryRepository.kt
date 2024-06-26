package com.example.timezoneaware.data.repository.timezone

import com.example.timezoneaware.data.model.InputTimezone
import com.example.timezoneaware.data.model.TimeZoneResponse
import com.example.timezoneaware.data.model.TimeZonesResponse
import com.example.timezoneaware.utils.DataResult

interface ITimeZoneRepositoryRepository {

    suspend fun addTimezone(objTimezone: InputTimezone): DataResult<TimeZoneResponse?>
    suspend fun getTimezoneListData(id:String): DataResult<TimeZonesResponse?>
    suspend fun deleteCityTimezone(id:String): DataResult<TimeZoneResponse?>
    suspend fun updateCityTimezone(id:String, objTimezone:InputTimezone): DataResult<TimeZoneResponse?>
    suspend fun searchCityTimezone(id:String,name:String): DataResult<TimeZonesResponse?>

}