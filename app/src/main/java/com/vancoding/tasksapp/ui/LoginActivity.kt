package com.vancoding.tasksapp.ui

import android.content.Intent
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.ActivityLoginBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.PreferencesManager
import com.vancoding.tasksapp.util.ToastUtils
import com.vancoding.tasksapp.util.ValidationUtil
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
                ToastUtils.showToast(this, "Please enter both username and password", binding.root)
            } else {
                mViewModel.getUser(username, password)
            }
        }

        binding.etUsername.inputType = InputType.TYPE_CLASS_TEXT

        binding.pwdState.setOnClickListener {
            showPassword(binding.etPassword, binding.pwdState);
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
                // Successful login
                PreferencesManager.saveUserCredentials(this, user.username, user.password)
                ToastUtils.showToast(this, "Login Successful", binding.root)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                // Handle case where username does not exist or incorrect password
                val username = binding.etUsername.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                if (username.isEmpty() || password.isEmpty()) {
                    ToastUtils.showToast(this, "Please enter both username and password", binding.root)
                } else {
                    ToastUtils.showToast(this, "User does not exist", binding.root)
                }
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
