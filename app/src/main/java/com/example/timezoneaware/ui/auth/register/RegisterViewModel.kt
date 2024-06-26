package com.example.timezoneaware.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezoneaware.concurrency.IAppDispatchers
import com.example.timezoneaware.data.model.AuthResponse
import com.example.timezoneaware.data.model.UserAuth
import com.example.timezoneaware.data.repository.auth.IAuthRepository
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.DataResult.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IAuthRepository,
) : ViewModel() {


    private val _authUser: MutableLiveData<DataResult<AuthResponse>?> = MutableLiveData()
    val authUser: LiveData<DataResult<AuthResponse>?> = _authUser
    val loading = MutableLiveData<Boolean>()

    fun setUpRegister(userAuth: UserAuth) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.register(userAuth) }

        when (result) {
            is GenericError -> {
                loading.value = false
                _authUser.value = GenericError(result.code, result.errorMessages)
            }
            is NetworkError -> {
                loading.value = false
                _authUser.value = NetworkError(result.networkError)
            }
            is Success -> {
                loading.value = false
                _authUser.value = Success(result.value!!)
            }
        }
    }

}