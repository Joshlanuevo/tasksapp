package com.vancoding.tasksapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.repository.TasksRepository
import com.vancoding.tasksapp.util.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel(private val repository: TasksRepository) : ViewModel() {

    private val _tasks = MutableLiveData<List<TasksBean>>()
    val tasks: LiveData<List<TasksBean>>
        get() = _tasks

    fun insert(task: TasksBean, context: Context) = viewModelScope.launch {
        repository.insert(task);
        loadTasks(context);
    }

    fun update(task: TasksBean, context: Context) = viewModelScope.launch {
        repository.update(task);
        loadTasks(context);
    }

    fun delete(task: TasksBean, context: Context) = viewModelScope.launch {
        repository.delete(task);
        loadTasks(context);
    }

    fun loadTasks(context: Context) = viewModelScope.launch {
        val userId = PreferencesManager.getUserId(context)
        if (userId != null) {
            val tasks = repository.getTasksForUser(userId)
            _tasks.postValue(tasks)
        }
    }

    suspend fun getTasksForUser(userId: Int) {
        withContext(Dispatchers.IO) {
            val tasks = repository.getTasksForUser(userId)
            _tasks.postValue(tasks)
        }
    }
}
