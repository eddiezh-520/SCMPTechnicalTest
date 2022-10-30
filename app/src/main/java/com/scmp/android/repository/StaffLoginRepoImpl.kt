package com.scmp.android.repository


import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.scmp.android.model.*
import com.scmp.android.network.StaffLoginService
import com.scmp.android.paging.StaffInfoPagingSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.Error
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StaffLoginRepoImpl(
    val staffLoginService: StaffLoginService
): StaffLoginRepo {

    override suspend fun login(delay: Int?, body: UserInfo): LoginToken = suspendCoroutine { continuation ->

            staffLoginService.login(delay, body).enqueue(object : Callback<LoginToken>{
                override fun onResponse(call: Call<LoginToken>, response: Response<LoginToken>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            continuation.resume(LoginToken(token = body.token, isSuccess = true))
                        }
                    } else {
                        val builder = GsonBuilder()
                        val type = object: TypeToken<ErrorInfo>() {}.type
                        builder.registerTypeAdapter(type , ErrorInfoJsonDeserializer())
                        val gson = builder.create()
                        val errorInfo = gson.fromJson(response.errorBody()?.string(), ErrorInfo::class.java)
                        Log.d("Eddie","test error message:${errorInfo.error}")
                        continuation.resume(LoginToken(isSuccess = false, errorMsg = errorInfo.error))
                    }
                }

                override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        continuation.resume(LoginToken(isSuccess = false, errorMsg = "TIME_OUT_ERROR"))
                    } else if (t is UnknownHostException) {
                        continuation.resume(LoginToken(isSuccess = false, errorMsg = "NETWORK_ERROR"))
                    } else {
                        continuation.resume(LoginToken(isSuccess = false, errorMsg = t.localizedMessage ?: ""))
                    }
                }

            })
    }

    override fun getStaffs(): Flow<PagingData<StaffInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1
            ),
            pagingSourceFactory = { StaffInfoPagingSource(staffLoginService) }
        ).flow
    }

    class ErrorInfoJsonDeserializer: JsonDeserializer<ErrorInfo> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ErrorInfo {
            if(json.isJsonObject) {
                val obj = json.asJsonObject
                var errorMessage = ""
                try {
                    errorMessage = obj.get("error").asString
                } catch(e: java.lang.Exception) {
                    errorMessage = obj.get("error").asJsonObject.get("error").asString
                }
                return ErrorInfo(errorMessage)
            }
            throw JsonParseException("not json object")
        }

    }


}