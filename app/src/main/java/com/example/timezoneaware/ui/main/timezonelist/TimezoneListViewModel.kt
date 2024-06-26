package com.example.timezoneaware.ui.main.timezonelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezoneaware.concurrency.IAppDispatchers
import com.example.timezoneaware.data.model.*
import com.example.timezoneaware.data.repository.timezone.ITimeZoneRepositoryRepository
import com.example.timezoneaware.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimezoneListViewModel  @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: ITimeZoneRepositoryRepository,
) : ViewModel() {

    private val _timezone: MutableLiveData<DataResult<TimeZonesResponse>?> = MutableLiveData()
    val data: LiveData<DataResult<TimeZonesResponse>?> = _timezone
    val loading = MutableLiveData<Boolean>()



    fun getTimezonesList(id:String) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.getTimezoneListData(id) }
        when (result) {
            is DataResult.GenericError -> {
                _timezone.value = DataResult.GenericError(result.code, result.errorMessages)
                loading.value = false
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _timezone.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                _timezone.value = DataResult.Success(result.value!!)
                loading.value = false
            }
        }
    }

    fun searchCity(id:String,name:String) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.searchCityTimezone(id,name) }
        when (result) {
            is DataResult.GenericError -> {
                _timezone.value = DataResult.GenericError(result.code, result.errorMessages)
                loading.value = false
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _timezone.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                _timezone.value = DataResult.Success(result.value!!)
                loading.value = false
            }
        }
    }


    private val _response: MutableLiveData<DataResult<TimeZoneResponse>?> = MutableLiveData()
    val simpledata: LiveData<DataResult<TimeZoneResponse>?> = _response
    fun deleteTimeZone(id:String) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.deleteCityTimezone(id) }
        when (result) {
            is DataResult.GenericError -> {
                _response.value = DataResult.GenericError(result.code, result.errorMessages)
                loading.value = false
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _response.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                _response.value = DataResult.Success(result.value!!)
                loading.value = false
            }
        }
    }


}