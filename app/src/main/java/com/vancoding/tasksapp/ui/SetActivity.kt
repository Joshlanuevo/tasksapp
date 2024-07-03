package com.vancoding.tasksapp.ui

import android.content.Intent
import com.vancoding.tasksapp.databinding.ActivitySetBinding
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.util.PreferencesManager

class SetActivity : BaseActivity() {
    private lateinit var bindView: ActivitySetBinding;

    override fun initView() {
        bindView = ActivitySetBinding.inflate(layoutInflater);
        setContentView(bindView.root);

        bindView.btnLogout.setOnClickListener {
            logoutUser();
        }
    }

    override fun requestData() {}

    override fun observeCallBack() {}

    private fun logoutUser() {
        // Clear stored user credentials
        PreferencesManager.clearUserCredentials(this)
        val intent = Intent(this, LoginActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        startActivity(intent);
        finish();
    }
}