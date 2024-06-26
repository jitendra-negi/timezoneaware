package com.example.timezoneaware.ui.auth.login

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
class LoginViewModel @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IAuthRepository,
) : ViewModel() {

    private val _user: MutableLiveData<DataResult<AuthResponse>?> = MutableLiveData()
    val currentUser: LiveData<DataResult<AuthResponse>?> = _user
    val loading = MutableLiveData<Boolean>()

    fun logInUser(userAuth: UserAuth) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.login(userAuth) }

        when (result) {
            is GenericError -> {
                _user.value = GenericError(result.code, result.errorMessages)
                loading.value = false
            }
            is NetworkError -> {
                loading.value = false
                _user.value = NetworkError(result.networkError)
            }
            is Success -> {
                _user.value = Success(result.value!!)
                loading.value = false
            }

        }
    }
}