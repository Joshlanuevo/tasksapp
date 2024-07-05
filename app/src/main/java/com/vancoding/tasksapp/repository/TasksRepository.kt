package com.vancoding.tasksapp.repository

import com.vancoding.tasksapp.bean.TasksBean
import com.vancoding.tasksapp.db.TasksDao

class TasksRepository(private val tasksDao: TasksDao) {

    suspend fun insert(task: TasksBean): Long {
        return tasksDao.insert(task);
    }

    suspend fun update(task: TasksBean): Int {
        return tasksDao.update(task);
    }

    suspend fun getAllTasks(): List<TasksBean> {
        return tasksDao.getAllTasks();
    }
}