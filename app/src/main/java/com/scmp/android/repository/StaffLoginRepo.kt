package com.scmp.android.repository

import com.scmp.android.model.LoginToken
import com.scmp.android.model.StaffList
import com.scmp.android.model.UserInfo
import okhttp3.ResponseBody
import retrofit2.http.Body

interface StaffLoginRepo {
    suspend fun login(delay: Int?, body: UserInfo): LoginToken
    suspend fun getStaffs(page: Int): StaffList
}