package com.example.timezoneaware.ui.main.addtimezone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezoneaware.concurrency.IAppDispatchers
import com.example.timezoneaware.data.model.InputTimezone
import com.example.timezoneaware.data.model.TimeZoneResponse
import com.example.timezoneaware.data.repository.timezone.ITimeZoneRepositoryRepository
import com.example.timezoneaware.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddTimezoneViewModel  @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: ITimeZoneRepositoryRepository,
) : ViewModel() {

    private val _timezone: MutableLiveData<DataResult<TimeZoneResponse>?> = MutableLiveData()
    val data: LiveData<DataResult<TimeZoneResponse>?> = _timezone
    val loading = MutableLiveData<Boolean>()

    fun addTimezoneData(objCityTimezone: InputTimezone) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.addTimezone(objCityTimezone) }

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

    fun updateTimezoneData(id: String,objTimezone: InputTimezone) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.updateCityTimezone(id,objTimezone) }
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
}