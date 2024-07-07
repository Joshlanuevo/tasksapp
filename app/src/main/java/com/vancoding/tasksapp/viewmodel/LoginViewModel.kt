package com.vancoding.tasksapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {

    private val _user = MutableLiveData<UserBean?>()
    val user: LiveData<UserBean?>
        get() = _user

    fun getUser(username: String, password: String) {
        viewModelScope.launch {
            val result = repository.getUser(username, password)
            _user.postValue(result)
            result?.let { user ->
                PreferencesManager.setUserId(getApplication(), user.id)
            }
        }
    }
}
