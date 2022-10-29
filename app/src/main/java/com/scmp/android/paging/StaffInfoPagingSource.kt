package com.scmp.android.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scmp.android.model.StaffInfo
import com.scmp.android.network.StaffLoginService
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

private const val DEFAULT_PAGE_INDEX = 1
class StaffInfoPagingSource(private val staffLoginService: StaffLoginService) :PagingSource<Int,StaffInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StaffInfo> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = staffLoginService.getStaffs(page)
            Log.d("Eddie","test response:$response")
            Log.d("Eddie","test response page:${response.page}")
            LoadResult.Page(
                response.data,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.data.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StaffInfo>): Int? {
        return null
    }

}