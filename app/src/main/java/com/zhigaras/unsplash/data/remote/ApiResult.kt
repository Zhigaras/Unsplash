package com.zhigaras.unsplash.data.remote

import android.content.res.Resources
import androidx.annotation.StringRes
import com.zhigaras.unsplash.R

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}

sealed class ApiResult<out T>(
    val status: ApiStatus,
    val data: T?,
    val errorInfo: ErrorInfo?
) {
    
    data class Success<out R>(val _data: R?) : ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        errorInfo = null
    )
    
    data class Error(
        val exception: String =  Resources.getSystem().getString(R.string.something_went_wrong),
        val _needToGoBack: Boolean = false,
//        val _errorButtonText: Int = R.string.try_again
    ) : ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        errorInfo = ErrorInfo(
            message = exception,
            needToGoBack = _needToGoBack,
//            errorButtonText = _errorButtonText
        )
    )
    
    class Loading<out R> : ApiResult<R>(
        status = ApiStatus.LOADING,
        data = null,
        errorInfo = null
    )
}

class ErrorInfo(
    val message: String,
    val needToGoBack: Boolean,
//    val errorButtonText: Int
)