package com.vancoding.tasksapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository): ViewModel() {

    private val _user = MutableLiveData<UserBean?>()
    val user: LiveData<UserBean?>
        get() = _user

    fun insert(username: String, password: String) = viewModelScope.launch {
        val user = UserBean(username = username, password = password)
        val userId = repository.insert(user)
        if (userId > 0) {
            _user.postValue(user)
        } else {
            _user.postValue(null)
        }
    }
}