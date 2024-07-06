package com.vancoding.tasksapp.viewmodel

import MineViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.repository.UserRepository

class MineViewModelFactory(private val application: Application, private val repository: UserRepository) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MineViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
