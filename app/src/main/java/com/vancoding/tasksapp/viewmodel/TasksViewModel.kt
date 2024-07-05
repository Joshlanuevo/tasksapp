package com.vancoding.tasksapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.repository.TasksRepository
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: TasksRepository): ViewModel() {

    private val _tasks = MutableLiveData<List<TasksBean>>()
    val tasks: LiveData<List<TasksBean>>
        get() = _tasks

    init {
        loadTasks();
    }

    fun insert(task: TasksBean) = viewModelScope.launch {
        repository.insert(task);
        loadTasks();
    }

    fun update(task: TasksBean) = viewModelScope.launch {
        repository.update(task);
        loadTasks();
    }

    fun loadTasks() = viewModelScope.launch {
        val tasks = repository.getAllTasks();
        _tasks.value = tasks
    }
}