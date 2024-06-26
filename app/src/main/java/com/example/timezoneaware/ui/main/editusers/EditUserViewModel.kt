package com.example.timezoneaware.ui.main.editusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezoneaware.concurrency.IAppDispatchers
import com.example.timezoneaware.data.model.*
import com.example.timezoneaware.data.repository.user.IUsersRepository
import com.example.timezoneaware.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IUsersRepository,
) : ViewModel() {

    private val _user: MutableLiveData<DataResult<CustomResponse>?> = MutableLiveData()
    val data: LiveData<DataResult<CustomResponse>?> = _user
    val loading = MutableLiveData<Boolean>()

  /*  fun addTimezoneData(objCityTimezone: InputTimezone) = viewModelScope.launch {
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
    }*/

    fun updateUserData(id: Int,userId: Int,user: InputUserData) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.updateUserData(id,userId,user) }
        when (result) {
            is DataResult.GenericError -> {
                _user.value = DataResult.GenericError(result.code, result.errorMessages)
                loading.value = false
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _user.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                _user.value = DataResult.Success(result.value!!)
                loading.value = false
            }
        }
    }
}