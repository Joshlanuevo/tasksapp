package com.vancoding.tasksapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vancoding.tasksapp.R
import com.vancoding.tasksapp.adapter.UserWithTasksAdapter
import com.vancoding.tasksapp.bean.UserWithTasksBean
import com.vancoding.tasksapp.databinding.FragmentDiscoverBinding
import com.vancoding.tasksapp.mvvm.BaseFragment
import com.vancoding.tasksapp.viewmodel.DiscoverViewModel
import java.util.*

class DiscoverFragment : BaseFragment(R.layout.fragment_discover) {
    private lateinit var mViewModel: DiscoverViewModel
    private lateinit var mAdapter: UserWithTasksAdapter

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private var originalUsersList: List<UserWithTasksBean> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeCallBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initView() {
        binding.ivLogo.load(R.mipmap.ic_launcher)

        mAdapter = UserWithTasksAdapter()
        binding.listUsers.adapter = mAdapter
        binding.listUsers.layoutManager = GridLayoutManager(requireContext(), 2)

        mViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)

        binding.refreshLayout.setOnRefreshListener { requestData() }

        // Set up search functionality
        binding.tvSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter the list based on search query
                mAdapter.submitList(filterUsers(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }
        })

        binding.tvSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform search or filter tasks when "Enter" is pressed
                val query = binding.tvSearch.text.toString().trim()
                mAdapter.submitList(filterUsers(query))
                // Hide the keyboard
                hideKeyboard()
                true // Consume the action
            } else {
                false // Continue with default behavior
            }
        }
    }

    override fun requestData() {
        mViewModel.getUserWithTasks()
        binding.refreshLayout.isRefreshing = false
    }

    override fun observeCallBack() {
        mViewModel.usersWithTasks.observe(viewLifecycleOwner) { userWithTasks ->
            userWithTasks?.let {
                originalUsersList = it
                mAdapter.setUsers(it)
            }
        }
    }

    private fun filterUsers(query: String): List<UserWithTasksBean> {
        return originalUsersList.filter { user ->
            user.user.username.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
            user.user.nickname.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.tvSearch.windowToken, 0)
    }
}
