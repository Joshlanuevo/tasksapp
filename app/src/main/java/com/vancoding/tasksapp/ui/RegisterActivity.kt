package com.vancoding.tasksapp.ui

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.activity.viewModels
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.databinding.ActivityRegisterBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.ValidationUtil
import com.vancoding.tasksapp.viewmodel.LoginViewModelFactory
import com.vancoding.tasksapp.viewmodel.RegisterViewModel
import com.vancoding.tasksapp.viewmodel.RegisterViewModelFactory

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userRepository by lazy { UserRepository(UserDb.getDatabase(this).userDao) }
    private val mViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(application, userRepository)
    }

    override fun initView() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim();
            val password = binding.etPassword.text.toString().trim();

            if (!ValidationUtil.isUsernameValid(username)) {
                Toast.makeText(this, "Invalid username (minimum 8 characters)", Toast.LENGTH_SHORT).show()
            } else if (!ValidationUtil.isPasswordValid(password)) {
                Toast.makeText(this, "Invalid password (minimum 8 characters)", Toast.LENGTH_SHORT).show()
            } else {
                mViewModel.insert(username, password)
            }
        }

        binding.BtnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {
        mViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        })
    }
}