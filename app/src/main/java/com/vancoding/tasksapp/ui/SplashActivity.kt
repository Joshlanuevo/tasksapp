package com.vancoding.tasksapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vancoding.tasksapp.databinding.ActivityHomeBinding
import com.vancoding.tasksapp.databinding.ActivitySplashBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.viewmodel.LoginViewModel
import com.vancoding.tasksapp.viewmodel.LoginViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var bindView: ActivitySplashBinding;
    private val mViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(UserRepository(UserDb.getDatabase(this).userDao))
    }
    override fun initView() {
        bindView = ActivitySplashBinding.inflate(layoutInflater);
        setContentView(bindView.root);

        checkUserLoginStatus()
    }

    override fun requestData() {}

    override fun observeCallBack() {}

    // Placeholder method for checking if the user is logged in
    private fun checkUserLoginStatus() {
        val credentials = PreferencesManager.getUserCredentials(this);
        if (credentials != null) {
            val (username, password) = credentials;
            mViewModel.getUser(username, password)
            mViewModel.user.observe(this, Observer { user ->
                val isLoggedIn = user != null;
                Log.d("SplashActivity", "User logged in status: $isLoggedIn");
                navigateToAppropriateActivity(isLoggedIn);
            })
        } else {
            navigateToAppropriateActivity(false)
        }
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
}