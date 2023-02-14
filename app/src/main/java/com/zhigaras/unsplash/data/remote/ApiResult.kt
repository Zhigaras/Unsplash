package com.zhigaras.unsplash.data.remote

import android.content.res.Resources
import com.zhigaras.unsplash.R

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING,
    NOT_LOADED_YET
}

sealed class ApiResult<out T>(
    val status: ApiStatus,
    val data: T?,
    val errorMessage: String?
) {
    
    data class Success<out R>(val _data: R?) : ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        errorMessage = null
    )
    
    data class Error(
        val exception: String = Resources.getSystem().getString(R.string.something_went_wrong)
    ) : ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        errorMessage = exception
    )
    
    
    class Loading<out R> : ApiResult<R>(
        status = ApiStatus.LOADING,
        data = null,
        errorMessage = null
    )
    
    class NotLoadedYet<out R> : ApiResult<R>(
        status = ApiStatus.NOT_LOADED_YET,
        data = null,
        errorMessage = null
    )
}