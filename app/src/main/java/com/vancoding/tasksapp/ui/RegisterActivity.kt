package com.vancoding.tasksapp.ui

import android.content.Intent
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.activity.viewModels
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.bean.UserBean
import com.vancoding.tasksapp.databinding.ActivityRegisterBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.ToastUtils
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
                ToastUtils.showToast(this, "Invalid username (minimum 8 characters)", it);
            } else if (!ValidationUtil.isPasswordValid(password)) {
                ToastUtils.showToast(this, "Invalid password (minimum 8 characters)", it);
            } else {
                mViewModel.insert(username, password);
            }
        }

        binding.etUsername.inputType = InputType.TYPE_CLASS_TEXT

        binding.pwdState.setOnClickListener {
            showPassword(binding.etPassword, binding.pwdState);
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
                ToastUtils.showToast(this, "Register Successful", binding.root)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                ToastUtils.showToast(this, "Username already exists", binding.root)
            }
        })
    }

    private fun showPassword(edit: EditText, iv: ImageView) {
        edit.requestFocus();
        val show = edit.transformationMethod is PasswordTransformationMethod;
        edit.transformationMethod =
            if (show) HideReturnsTransformationMethod.getInstance()
            else PasswordTransformationMethod.getInstance()
        iv.setImageResource(if (show) R.drawable.ic_pwd_open else R.drawable.ic_pwd_close);
        edit.setSelection(edit.text.length);
    }
}