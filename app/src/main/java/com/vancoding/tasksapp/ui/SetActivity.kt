package com.vancoding.tasksapp.ui

import com.vancoding.tasksapp.databinding.ActivitySetBinding
import com.vancoding.tasksapp.mvvm.BaseActivity

class SetActivity : BaseActivity() {
    private lateinit var bindView: ActivitySetBinding;

    override fun initView() {
        bindView = ActivitySetBinding.inflate(layoutInflater);
        setContentView(bindView.root);
    }

    override fun requestData() {}

    override fun observeCallBack() {}
}