package com.vancoding.tasksapp.ui

import com.vancoding.tasksapp.databinding.ActivityHomeBinding
import com.vancoding.tasksapp.mvvm.BaseActivity

class HomeActivity : BaseActivity() {
    private lateinit var bindView : ActivityHomeBinding
    override fun initView() {
        bindView = ActivityHomeBinding.inflate(layoutInflater);
        setContentView(bindView.root);
    }

    override fun requestData() {}

    override fun observeCallBack() {}

}