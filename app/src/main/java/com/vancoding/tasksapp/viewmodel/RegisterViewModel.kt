package com.vancoding.tasksapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {

    private val _user = MutableLiveData<UserBean?>()
    val user: LiveData<UserBean?>
        get() = _user

    fun insert(username: String, password: String) = viewModelScope.launch {
        val nickname = "User${System.currentTimeMillis()}"
        val user = UserBean(nickname = nickname, username = username, password = password)
        val userId = repository.insert(user)
        if (userId > 0) {
            user.id = userId.toInt()
            _user.postValue(user)
            PreferencesManager.setUserId(getApplication(), userId.toInt())
        } else {
            _user.postValue(null)
        }
    }
}