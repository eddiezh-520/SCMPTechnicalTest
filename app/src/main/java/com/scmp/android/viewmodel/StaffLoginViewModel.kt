package com.scmp.android.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scmp.android.adapter.StaffInfoAdapter
import com.scmp.android.model.*
import com.scmp.android.repository.StaffLoginRepo
import com.scmp.android.util.Constant.INVALID_TOKEN
import com.scmp.android.util.Constant.PASSWORD_OR_ACCOUNT_ERROR
import com.scmp.android.util.Constant.PASSWORD_PATTERN
import com.scmp.android.util.Constant.SUCCESS_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffLoginViewModel @Inject constructor(
    private val staffLoginRepo: StaffLoginRepo
) : ViewModel() {

    companion object {
        enum class UserInfoChecking {
            EMAIL_OR_PASSWORD_EMPTY,
            EMAIL_INVALID,
            EMAIL_VALID,
            PASSWORD_INVALID,
            PASSWORD_VALID
        }
    }

    val loginStatus = MutableLiveData<ApiResult<LoginToken?>>()

    private val _email = MutableLiveData<String>()

    private val _password = MutableLiveData<String>()

    val userInfoChecking = MutableLiveData<UserInfoChecking>()

    val resultToken = MutableLiveData<String>()

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, exception ->
        loginStatus.postValue(ApiResult.Error(PASSWORD_OR_ACCOUNT_ERROR))
    }

    var _staffList = MutableLiveData<StaffList>()
    val staffList: MutableLiveData<StaffList> = _staffList

    var _loadMoreStaffList = MutableLiveData<StaffList>()
    val loadMoreStaffList: MutableLiveData<StaffList> = _loadMoreStaffList

    fun onEmailChange(email: String) {
        _email.postValue(email)
    }

    fun onPasswordChange(password: String) {
        _password.postValue(password)
    }

    private fun login(email: String ,password: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            loginStatus.postValue(ApiResult.Loading)
            /**
             * "eve.holt@reqres.in"
             * "cityslicka"
             */
            val loginMsg = staffLoginRepo.login(5, UserInfo(email = email, password = password))
            if (loginMsg.isSuccess) {
                    loginMsg.token?.let {
                        if (it == SUCCESS_TOKEN) {
                            resultToken.postValue(it)
                            loginStatus.postValue(ApiResult.Success(loginMsg))
                        } else {
                            loginStatus.postValue(ApiResult.Error(INVALID_TOKEN))
                        }
                    }?: run {
                        loginStatus.postValue(ApiResult.Error(INVALID_TOKEN))
                    }
            } else {

                loginStatus.postValue(ApiResult.Error(loginMsg.errorMsg))
            }
        }
    }

    fun goToLogin() {
        when(checkEmailIsValid(_email.value)) {
            UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY -> {
                userInfoChecking.postValue(UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY)
            }
            UserInfoChecking.EMAIL_INVALID -> {
                userInfoChecking.postValue(UserInfoChecking.EMAIL_INVALID)
            }
            UserInfoChecking.EMAIL_VALID -> {
                when(checkPasswordIsValid(_password.value)) {
                    UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY -> {
                        userInfoChecking.postValue(UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY)
                    }
                    UserInfoChecking.PASSWORD_INVALID -> {
                        userInfoChecking.postValue(UserInfoChecking.PASSWORD_INVALID)
                    }
                    UserInfoChecking.PASSWORD_VALID -> {
                        val resultEmail = _email.value
                        val resultPassword = _password.value
                        if (resultPassword != null && resultEmail != null) {
                            login(resultEmail,resultPassword)
                        }
                    }
                }
            }
        }
    }

    private fun checkEmailIsValid(email: String?): UserInfoChecking{
        email?.let {
            return if (it.isEmpty()) {
                UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY
            } else {
                if (Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    UserInfoChecking.EMAIL_VALID
                }else {
                    UserInfoChecking.EMAIL_INVALID
                }
            }
        }?: run {
            return UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY
        }
    }


    private fun checkPasswordIsValid(password: String?): UserInfoChecking {
        password?.let {
            return if (PASSWORD_PATTERN.toRegex().matches(password)) {
                UserInfoChecking.PASSWORD_VALID
            } else {
                UserInfoChecking.PASSWORD_INVALID
            }
        }?: run {
            return UserInfoChecking.EMAIL_OR_PASSWORD_EMPTY
        }
    }

//    fun getStaffs(): Flow<PagingData<StaffInfo>> {
//        return staffLoginRepo.getStaffs().cachedIn(viewModelScope)
//    }

    fun getStaffs(page: Int) {
        viewModelScope.launch {
            val staffList = staffLoginRepo.getStaffsInfo(page)
            _staffList.postValue(staffList)
        }
    }

    fun getLoadMoreStaffs(page: Int) {
        viewModelScope.launch {
            val staffList = staffLoginRepo.getStaffsInfo(page)
            _loadMoreStaffList.postValue(staffList)
        }
    }

    fun addLoading(mAdapter: StaffInfoAdapter) {
        var tempList = mutableListOf<Any>()
        tempList.addAll(mAdapter.currentList)
        tempList.add(LoadMore())
        mAdapter.submitList(tempList)
    }

    fun appendListData(mAdapter: StaffInfoAdapter,staffList: StaffList) {
        var resultList = mutableListOf<Any>()
        resultList.addAll(mAdapter.currentList)
        resultList.removeLast()
        resultList.addAll(staffList.data)
        mAdapter.submitList(resultList)
    }

}