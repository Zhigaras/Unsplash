package com.zhigaras.unsplash.data.remote

enum class AuthCheckStatus{
    LOADING,
    RESULT
}

sealed class AuthCheckResult<out T>(
    val status: AuthCheckStatus,
    val data: T?
) {

    data class Result<out R>(val _data: R?) : AuthCheckResult<R>(
        status = AuthCheckStatus.RESULT,
        data = _data
    )

    class Loading<out R> : AuthCheckResult<R>(
        status = AuthCheckStatus.LOADING,
        data = null
    )
}
