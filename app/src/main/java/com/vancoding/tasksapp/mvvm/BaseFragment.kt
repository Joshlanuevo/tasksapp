package com.vancoding.tasksapp.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open abstract class BaseFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId) {

    companion object {
//        var isLogin: Boolean =  !MMKVUtils.getUserId().isNullOrBlank()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()

        requestData()

        observeCallBack()

    }

    abstract fun initView()

    abstract fun requestData()

    abstract fun observeCallBack()

}