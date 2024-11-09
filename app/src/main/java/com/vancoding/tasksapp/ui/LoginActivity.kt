package com.vancoding.tasksapp.ui

import android.text.InputType
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vancoding.tasksapp.databinding.ActivityLoginBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.viewmodel.LoginViewModel
import com.vancoding.tasksapp.viewmodel.LoginViewModelFactory
import com.vancoding.tasksapp.extensions.navigateTo
import com.vancoding.tasksapp.extensions.showToast
import com.vancoding.tasksapp.extensions.togglePasswordVisibility

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userRepository by lazy { UserRepository(UserDb.getDatabase(this).userDao) }
    private val mViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(application, userRepository)
    }

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onViewReady()
    }

    override fun requestData() {}

    override fun observeCallBack() {
        mViewModel.user.observe(this, Observer { user ->
            binding.apply {
                user?.let {
                    PreferencesManager.saveUserCredentials(this@LoginActivity, user.username, user.password)
                    showToast("Login Successful")
                    navigateTo(HomeActivity::class.java)
                    finish()
                } ?: run {
                    showToast(
                        if (etUsername.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty())
                            "Please enter both username and password"
                        else "User does not exist"
                    )
                }
            }
        })
    }

    private fun onViewReady() {
        with(binding) {
            etUsername.inputType = InputType.TYPE_CLASS_TEXT
            btnLogin.setOnClickListener { onLoginClick() }
            pwdState.setOnClickListener {
                etPassword.togglePasswordVisibility(binding.pwdState, isVisible = true)
            }
            BtnRegister.setOnClickListener {
                navigateTo(RegisterActivity::class.java)
            }
        }
    }

    private fun onLoginClick() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Please enter both username and password")
        } else {
            mViewModel.getUser(username, password)
        }
    }
}
