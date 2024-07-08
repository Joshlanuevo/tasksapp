package com.vancoding.tasksapp.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.vancoding.tasksapp.R

object ToastUtils {

    fun showToast(context: Context, message: String, view: View) {
        // Hide the keyboard
        KeyboardUtils.hideKeyboard(view)

        // Create and show the custom toast with default style
        val inflater = LayoutInflater.from(context)
        val toastView = inflater.inflate(R.layout.layout_custom_toast, null)

        val textViewToast = toastView.findViewById<TextView>(R.id.textViewToast)
        textViewToast.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = toastView
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 250)
        toast.show()
    }
}
