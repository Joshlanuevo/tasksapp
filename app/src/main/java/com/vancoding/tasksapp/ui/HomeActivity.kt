package com.vancoding.tasksapp.ui

import androidx.fragment.app.Fragment
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.databinding.ActivityHomeBinding
import com.vancoding.tasksapp.mvvm.BaseActivity
import com.vancoding.tasksapp.ui.fragment.DiscoverFragment
import com.vancoding.tasksapp.ui.fragment.MineFragment
import com.vancoding.tasksapp.ui.fragment.TasksFragment

class HomeActivity : BaseActivity() {
    private lateinit var bindView : ActivityHomeBinding

    var mFragments = mutableListOf<Fragment>();
    var mIndex = 0
    var page = -1
    var mLastCheckedId = R.id.item_bottom_1;

    override fun initView() {
        bindView = ActivityHomeBinding.inflate(layoutInflater);
        setContentView(bindView.root);
    }

    override fun requestData() {
        mFragments.apply {
            add(TasksFragment())
            add(DiscoverFragment())
            add(MineFragment())
        }
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, mFragments[0]).commitAllowingStateLoss();
        setRadioGroupListener();
    }

    override fun observeCallBack() {}

    private fun setIndexSelected(index: Int) {
        if (mIndex == index) {
            return
        }
        page = index;
        val ft =supportFragmentManager.beginTransaction();
        // Hide
        ft.hide(mFragments[mIndex]);
        // Judging whether it is added
        if (!mFragments[index].isAdded) {
            ft.add(R.id.frameLayout, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commitAllowingStateLoss();
        // Assign again
        mIndex = index;
    }

    private fun setRadioGroupListener() {
        bindView.apply {
            radioGroupButton.setOnCheckedChangeListener {_, checkedId ->
                when (checkedId) {
                    R.id.item_bottom_1 -> {
                        setIndexSelected(0)
                    }

                    R.id.item_bottom_2 -> {
                        setIndexSelected(1)
                    }

                    R.id.item_bottom_3 -> {
                        setIndexSelected(2)
                    }
                }
                mLastCheckedId = checkedId;
            }
        }
    }

}