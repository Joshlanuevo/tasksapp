package com.vancoding.tasksapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.UserWithTasksBean
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.repository.UserRepository
import kotlinx.coroutines.launch

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    val usersWithTasks: LiveData<List<UserWithTasksBean>>

    init {
        val userDao = UserDb.getDatabase(application).userDao;
        repository = UserRepository(userDao);
        usersWithTasks = repository.getUsersWithTasks();
    }

    fun getUserWithTasks() = viewModelScope.launch {
        repository.getUsersWithTasks();
    }
}