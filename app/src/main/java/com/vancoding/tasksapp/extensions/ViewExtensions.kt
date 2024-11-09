package com.vancoding.tasksapp.extensions

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import com.vancoding.tasksapp.R

fun EditText.togglePasswordVisibility(icon: ImageView, isVisible: Boolean) {
    transformationMethod = if (isVisible)
        HideReturnsTransformationMethod.getInstance()
    else
        PasswordTransformationMethod.getInstance()

    icon.setImageResource(if (isVisible) R.drawable.ic_pwd_open else R.drawable.ic_pwd_close)
    setSelection(text.length)
}