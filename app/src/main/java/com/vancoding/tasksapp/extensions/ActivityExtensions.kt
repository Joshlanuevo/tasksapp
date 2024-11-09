package com.vancoding.tasksapp.extensions

import android.app.Activity
import android.content.Intent
import com.vancoding.tasksapp.util.ToastUtils

fun Activity.showToast(message: String) {
    ToastUtils.showToast(this, message, this.findViewById(android.R.id.content))
}

fun Activity.navigateTo(destination: Class<*>) {
    startActivity(Intent(this, destination))
}