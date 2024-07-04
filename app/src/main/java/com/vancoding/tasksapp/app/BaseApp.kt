package com.vancoding.tasksapp.app

import android.app.Application
import android.util.Log

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("MyApp", "Uncaught exception in thread ${thread.name}", throwable)
        }
    }
}