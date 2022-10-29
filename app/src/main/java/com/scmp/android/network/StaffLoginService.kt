package com.scmp.android.network

import com.scmp.android.model.LoginToken
import com.scmp.android.model.StaffList
import com.scmp.android.model.UserInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StaffLoginService {

    @POST("login")
    suspend fun login(
        @Query("delay") delay: Int?,
        @Body body: UserInfo
    ): LoginToken


    @GET("user")
    suspend fun getStaffs(
        @Query("page") page: Int
    ): StaffList
}