package com.vancoding.tasksapp.util

object ValidationUtil {
    // Function to validate username
    fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank() && username.length >= 8
    }

    // Function to validate password
    fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank() && password.length >= 8
    }
}