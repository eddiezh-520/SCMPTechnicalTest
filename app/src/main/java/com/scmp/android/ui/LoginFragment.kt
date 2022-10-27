package com.scmp.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.scmp.android.R
import com.scmp.android.databinding.FragmentLoginBinding
import com.scmp.android.model.ApiResult
import com.scmp.android.viewmodel.StaffLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val mBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val viewModel : StaffLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
    }

    private fun initListener() {
        mBinding.button.setOnClickListener {
            viewModel.login()
        }
    }

    private fun initObserver() {
        viewModel.loginStatus.observe(viewLifecycleOwner) {
            when(it) {
                is ApiResult.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                is ApiResult.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_staffFragment)
                }
                is ApiResult.Error -> {
                    mBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }

}