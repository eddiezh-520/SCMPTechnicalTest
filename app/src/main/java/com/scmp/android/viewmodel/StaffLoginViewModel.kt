package com.scmp.android.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scmp.android.model.UserInfo
import com.scmp.android.repository.StaffLoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffLoginViewModel @Inject constructor(
    private val staffLoginRepo: StaffLoginRepo
) : ViewModel() {

    fun login() {
        viewModelScope.launch {
            val loginResult = staffLoginRepo.login(5, UserInfo(email = "eve.holt@reqres.in", password = "cityslicka"))
            val staffResult = staffLoginRepo.getStaffs(page = 1)
            Log.d("Eddie","test loginResult:$loginResult")
            Log.d("Eddie","test staffResult:$staffResult")
        }
    }
}