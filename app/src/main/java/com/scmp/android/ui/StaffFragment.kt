package com.scmp.android.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.scmp.android.R
import com.scmp.android.adapter.StaffInfoAdapter
import com.scmp.android.databinding.FragmentLoginBinding
import com.scmp.android.databinding.FragmentStaffBinding
import com.scmp.android.viewmodel.StaffLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StaffFragment : Fragment() {

    val mBinding by lazy {
        FragmentStaffBinding.inflate(layoutInflater)
    }

    val mAdapter by lazy {
        StaffInfoAdapter(requireContext())
    }

    private val viewModel: StaffLoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView() {
        mBinding.token.text = viewModel.resultToken.value
        mBinding.recyclerview.adapter = mAdapter
    }


    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.getStaffs().collectLatest {
                mAdapter.submitData(it)
            }
        }
    }

}