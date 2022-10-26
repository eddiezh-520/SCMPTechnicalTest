package com.scmp.android.repository

import com.scmp.android.model.LoginToken
import com.scmp.android.model.StaffList
import com.scmp.android.model.UserInfo
import com.scmp.android.network.StaffLoginService
import okhttp3.ResponseBody

class StaffLoginRepoImpl(
    val staffLoginService: StaffLoginService
): StaffLoginRepo {

    override suspend fun login(delay: Int?, body: UserInfo): LoginToken {
        return staffLoginService.login(delay, body)
    }

    override suspend fun getStaffs(page: Int): StaffList {
        return staffLoginService.getStaffs(page)
    }
}