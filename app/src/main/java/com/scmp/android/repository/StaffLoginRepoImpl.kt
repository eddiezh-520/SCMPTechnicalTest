package com.scmp.android.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scmp.android.model.*
import com.scmp.android.network.StaffLoginService
import com.scmp.android.paging.StaffInfoPagingSource
import kotlinx.coroutines.flow.Flow

class StaffLoginRepoImpl(
    val staffLoginService: StaffLoginService
): StaffLoginRepo {

    override suspend fun login(delay: Int?, body: UserInfo): LoginToken {
        return staffLoginService.login(delay, body)
    }

    override fun getStaffs(): Flow<PagingData<StaffInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
            ),
            pagingSourceFactory = { StaffInfoPagingSource(staffLoginService) }
        ).flow
    }


}