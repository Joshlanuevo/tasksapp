package com.vancoding.tasksapp.ui

import android.widget.Toast
import com.vancoding.tasksapp.databinding.ActivityLoginBinding
import com.vancoding.tasksapp.mvvm.BaseActivity

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {}
        }

        binding.BtnRegister.setOnClickListener {
            // Handle register button click
            // Example: Navigate to RegisterActivity
            // startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}