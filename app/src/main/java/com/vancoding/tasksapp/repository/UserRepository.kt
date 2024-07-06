package com.vancoding.tasksapp.repository

import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.bean.UserWithTasksBean
import com.vancoding.tasksapp.db.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: UserBean): Long {
        return userDao.insert(user);
    }

    suspend fun getUser(username: String, password: String): UserBean? {
        return userDao.getUser(username, password)
    }

    suspend fun updateNickname(newNickname: String, userId: Int) {
        userDao.updateNickname(newNickname, userId)
    }

    suspend fun getUserById(userId: Int): UserBean? {
        return userDao.getUserById(userId)
    }

    suspend fun updateAvatar(userId: Int, avatarUrl: String) {
        userDao.updateAvatar(userId, avatarUrl)
    }

    suspend fun getUsersWithTasks(): List<UserWithTasksBean> {
        return userDao.getUsersWithTasks()
    }
}