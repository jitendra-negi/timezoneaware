package com.example.timezoneaware.ui.main.user

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
class UserListViewModel  @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val repository: IUsersRepository,
) : ViewModel() {

    private val _users: MutableLiveData<DataResult<UsersResponse>?> = MutableLiveData()
    val data: LiveData<DataResult<UsersResponse>?> = _users
    val loading = MutableLiveData<Boolean>()

    fun getUserList(id:String) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.getUserList(id) }
        when (result) {
            is DataResult.GenericError -> {
                _users.value = DataResult.GenericError(result.code, result.errorMessages)
                loading.value = false
            }
            is DataResult.NetworkError -> {
                loading.value = false
                _users.value = DataResult.NetworkError(result.networkError)
            }
            is DataResult.Success -> {
                _users.value = DataResult.Success(result.value!!)
                loading.value = false
            }
        }
    }


    private val _response: MutableLiveData<DataResult<CustomResponse>?> = MutableLiveData()
    val simpledata: LiveData<DataResult<CustomResponse>?> = _response
    fun deleteUserAccount(id:String,userId:Int) = viewModelScope.launch {
        loading.value = true
        val result = withContext(appDispatchers.io) { repository.deleteUserData(id,userId) }
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