package com.scmp.android.model

data class LoginToken(
    val token: String?="",
    @Transient
    val isSuccess: Boolean,
    @Transient
    val errorMsg: String?="",
)

data class ErrorInfo(
    val error: String
)

