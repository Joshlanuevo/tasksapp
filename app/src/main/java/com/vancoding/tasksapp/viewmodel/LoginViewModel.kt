package com.vancoding.tasksapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    private val _user = MutableLiveData<UserBean?>()
    val user: LiveData<UserBean?>
        get() = _user

    fun insert(user: UserBean) = viewModelScope.launch {
        repository.insert(user)
    }

    fun getUser(username: String, password: String) = viewModelScope.launch {
        val result = repository.getUser(username, password)
        _user.postValue(result)
    }
}