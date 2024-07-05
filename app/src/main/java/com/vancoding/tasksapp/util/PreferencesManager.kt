package com.vancoding.tasksapp.util

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USERNAME = "username"
    private const val KEY_PASSWORD = "password"
    private const val PREF_USER_ID = "user_id"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserCredentials(context: Context, username: String, password: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun getUserCredentials(context: Context): Pair<String, String>? {
        val prefs = getSharedPreferences(context)
        val username = prefs.getString(KEY_USERNAME, "") ?: ""
        val password = prefs.getString(KEY_PASSWORD, "") ?: ""
        return if (username.isNotEmpty() && password.isNotEmpty()) {
            Pair(username, password)
        } else {
            null
        }
    }

    fun clearUserCredentials(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }

    fun setUserId(context: Context, userId: Int) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(PREF_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (sharedPreferences.contains(PREF_USER_ID)) {
            sharedPreferences.getInt(PREF_USER_ID , -1)
        } else {
            null
        }
    }

    fun clearUserId(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(PREF_USER_ID).apply()
    }

//    fun getUserId(context: Context): Int {
//        val preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        return preferences.getInt(PREF_USER_ID, -1) // Return -1 as default or handle appropriately
//    }
//
//    fun setUserId(context: Context, userId: Int) {
//        val preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        preferences.edit().putInt(PREF_USER_ID, userId).apply()
//    }
//
//    fun clearUserId(context: Context) {
//        val preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        preferences.edit().remove(PREF_USER_ID).apply()
//    }
}