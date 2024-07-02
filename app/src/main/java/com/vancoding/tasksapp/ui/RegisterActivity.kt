package com.vancoding.tasksapp.ui

import android.content.Intent
import com.vancoding.tasksapp.databinding.ActivityRegisterBinding
import com.vancoding.tasksapp.mvvm.BaseActivity

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding;

    override fun initView() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}