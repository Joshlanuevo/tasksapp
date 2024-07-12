package com.vancoding.tasksapp.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.vancoding.tasksapp.R

object ToastUtils {

    fun showToast(context: Context, message: String, view: View) {
        // Hide the keyboard
        hideKeyboard(context, view)

        // Create and show the custom toast
        val inflater = LayoutInflater.from(context)
        val toastView = inflater.inflate(R.layout.layout_custom_toast, null)

        val textViewToast = toastView.findViewById<TextView>(R.id.textViewToast)
        textViewToast.text = message

        Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 150)
            duration = Toast.LENGTH_SHORT
            setView(toastView)
            show()
        }
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}