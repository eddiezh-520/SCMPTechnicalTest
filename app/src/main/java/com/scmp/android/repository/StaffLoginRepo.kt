package com.scmp.android.repository

import androidx.paging.PagingData
import com.scmp.android.model.*
import kotlinx.coroutines.flow.Flow

interface StaffLoginRepo {
    suspend fun login(delay: Int?, body: UserInfo): LoginToken
    fun getStaffs(): Flow<PagingData<StaffInfo>>
}