package com.scmp.android.model

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    object Loading : ApiResult<Nothing>()
    class Error(val errorMsg: String? = "") : ApiResult<Nothing>()
}