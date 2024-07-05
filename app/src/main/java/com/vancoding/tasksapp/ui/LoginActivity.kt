package com.vancoding.tasksapp.ui

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vancoding.tasksapp.databinding.ActivityLoginBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.viewmodel.LoginViewModel
import com.vancoding.tasksapp.viewmodel.LoginViewModelFactory

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userRepository by lazy { UserRepository(UserDb.getDatabase(this).userDao) }
    private val mViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(application, userRepository)
    }

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                mViewModel.getUser(username, password)
            }
        }

        binding.BtnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun requestData() {
        // No specific data requests in this context
    }

    override fun observeCallBack() {
        mViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                PreferencesManager.saveUserCredentials(this, user.username, user.password)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
