package com.vancoding.tasksapp.ui

import MineViewModel
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.ActivityModifyPasswordBinding
import com.vancoding.tasksapp.db.UserDb
import com.vancoding.tasksapp.extensions.showToast
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.repository.UserRepository
import com.vancoding.tasksapp.util.ToastUtils
import com.vancoding.tasksapp.viewmodel.MineViewModelFactory

class ModifyPasswordActivity : BaseActivity() {
    private lateinit var bindView: ActivityModifyPasswordBinding;
    private lateinit var mViewModel: MineViewModel;

    override fun initView() {
        bindView = ActivityModifyPasswordBinding.inflate(layoutInflater);
        setContentView(bindView.root);

        val userRepository = UserRepository(UserDb.getDatabase(applicationContext).userDao);
        mViewModel = ViewModelProvider(this, MineViewModelFactory(application, userRepository)).get(MineViewModel::class.java);

        bindView.toolbar.run {
            viewBack.setOnClickListener {
                finish()
            }
            tvTitle.text = "Edit Password"
        }

        bindView.ivOldPasswordState.setOnClickListener {
            showPassword(bindView.etOldPassword, bindView.ivOldPasswordState)
        }

        bindView.ivNewPasswordState.setOnClickListener {
            showPassword(bindView.etNewPassword, bindView.ivNewPasswordState)
        }

        bindView.ivConfirmPasswordState.setOnClickListener {
            showPassword(bindView.etConfirmPassword, bindView.ivConfirmPasswordState)
        }

        bindView.btnSave.setOnClickListener {
            val oldPassword = bindView.etOldPassword.text.toString()
            val newPassword = bindView.etNewPassword.text.toString()
            val confirmPassword = bindView.etConfirmPassword.text.toString()

            if (validateInput(oldPassword, newPassword, confirmPassword)) {
                mViewModel.changePassword(oldPassword, newPassword).observe(this, Observer { result ->
                    result.onSuccess { isSuccess ->
                        if (isSuccess) {
                            ToastUtils.showToast(this@ModifyPasswordActivity, "Password changed successfully", bindView.root)
                            finish()
                        } else {
                            ToastUtils.showToast(this@ModifyPasswordActivity, "Incorrect old password", bindView.root)
                        }
                    }
                    result.onFailure { e ->
                        ToastUtils.showToast(this@ModifyPasswordActivity, "Error changing password: ${e.message}", bindView.root)
                    }
                })
            }
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}

    private fun validateInput(oldPassword: String, newPassword: String, confirmPassword: String): Boolean {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showToast("All fields are required")
            return false
        }
        if (newPassword != confirmPassword) {
            showToast("Passwords do not match")
            return false
        }
        return true
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