package com.scmp.android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scmp.android.model.ApiResult
import com.scmp.android.model.LoginToken
import com.scmp.android.model.UserInfo
import com.scmp.android.repository.StaffLoginRepo
import com.scmp.android.util.Constant.SUCCESS_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffLoginViewModel @Inject constructor(
    private val staffLoginRepo: StaffLoginRepo
) : ViewModel() {

    val loginStatus = MutableLiveData<ApiResult<LoginToken?>>()

    fun login() {
        viewModelScope.launch {
            loginStatus.postValue(ApiResult.Loading)
            val loginMsg = staffLoginRepo.login(5, UserInfo(email = "eve.holt@reqres.in", password = "cityslicka"))
                if (loginMsg.token == SUCCESS_TOKEN) {
                    loginStatus.postValue(ApiResult.Success(loginMsg))
                } else {
                    loginStatus.postValue(ApiResult.Error())
                }
        }
    }

    //            val loginResult = staffLoginRepo.login(5, UserInfo(email = "eve.holt@reqres.in", password = "cityslicka"))
//            val staffResult = staffLoginRepo.getStaffs(page = 1)
//            Log.d("Eddie","test loginResult:$loginResult")
//            Log.d("Eddie","test staffResult:$staffResult")
}