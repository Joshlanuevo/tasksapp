package com.vancoding.tasksapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.vancoding.tasksapp.mvvm.BaseActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        // Placeholder for checking if the user is logged in
        val isLoggedIn = checkUserLoginStatus()

        Log.d("SplashActivity", "User logged in status: $isLoggedIn")

        navigateToAppropriateActivity(isLoggedIn)
    }

    private fun navigateToAppropriateActivity(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            // User is logged in, navigate to MainActivity
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        } else {
            // User is not logged in, navigate to LoginActivity
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
        finish()
    }

    // Placeholder method for checking if the user is logged in
    private fun checkUserLoginStatus(): Boolean {
        // Implement your actual login check logic here
        // For now, return false to always show LoginActivity
        return true;
    }

    override fun initView() {}

    override fun requestData() {}

    override fun observeCallBack() {}
}