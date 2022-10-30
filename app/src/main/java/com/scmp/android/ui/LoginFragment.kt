package com.scmp.android.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.scmp.android.R
import com.scmp.android.databinding.FragmentLoginBinding
import com.scmp.android.model.ApiResult
import com.scmp.android.viewmodel.StaffLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.scmp.android.viewmodel.StaffLoginViewModel.Companion.UserInfoChecking.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val mBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
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
        initListener()
        initObserver()
    }

    private fun initListener() {
        mBinding.button.setOnClickListener {
            viewModel.goToLogin()
        }
        mBinding.editTextTextPersonName.setupOnTextChanged { s -> viewModel.onEmailChange(s) }
        mBinding.editTextTextPassword.setupOnTextChanged { s -> viewModel.onPasswordChange(s) }
    }

    private fun initObserver() {
        viewModel.loginStatus.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                is ApiResult.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_staffFragment)
                    viewModel.loginStatus.postValue(null)
                }
                is ApiResult.Error -> {
                    mBinding.progressBar.visibility = View.GONE
                    Toast.makeText(context, it.errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.userInfoChecking.observe(viewLifecycleOwner) {
            when (it) {
                EMAIL_OR_PASSWORD_EMPTY -> {
                    mBinding.textView2.showErrorMsg(requireContext().resources.getString(R.string.error_password_or_email_not_empty_requirement))
                }
                EMAIL_INVALID -> {
                    mBinding.textView2.showErrorMsg(requireContext().resources.getString(R.string.error_msg_email_format_requirement))
                }
                PASSWORD_INVALID -> {
                    mBinding.textView3.showErrorMsg(requireContext().resources.getString(R.string.error_msg_password_format_requirement))
                }
            }
        }
    }

    private fun EditText.setupOnTextChanged(callback: (s: String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                callback.invoke(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun TextView.showErrorMsg(errorMsg: String) {
        lifecycleScope.launch {
            val mSlideBottom = Slide()
            mSlideBottom.slideEdge = Gravity.RIGHT
            mSlideBottom.duration = 600
            TransitionManager.beginDelayedTransition(mBinding.root, mSlideBottom)
            apply {
                visibility = View.VISIBLE
                text = errorMsg
                setTextColor(context.resources.getColor(R.color.red))
                delay(5000)
                TransitionManager.beginDelayedTransition(mBinding.root, mSlideBottom)
                visibility = View.GONE
            }
        }
    }


}