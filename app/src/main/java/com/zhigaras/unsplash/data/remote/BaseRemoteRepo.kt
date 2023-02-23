package com.zhigaras.unsplash.data.remote

import android.util.Log
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRemoteRepo(
    private val ioDispatcher: CoroutineDispatcher
) {
    
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): ApiResult<T> {
        return withContext(ioDispatcher) {
//            try {
            val response = apiToBeCalled()
            Log.d("AAA response", response.isSuccessful.toString())
            
            if (response.isSuccessful) {
                ApiResult.Success(_data = response.body())
            } else if (response.code() == 403) {
                ApiResult.Error(exception = "Rate Limit Exceeded")
            } else {
                ApiResult.Error(exception = "Check your internet connection")
            }
        }
//            } catch (e: HttpException) {
//                ApiResult.Error(
//                    exception = e.message ?: "1Resources.getSystem().getString(R.string.something_went_wrong)"
//                )
//            } catch (e: IOException) {
//                ApiResult.Error("Resources.getSystem().getString(R.string.check_connection)")
//            } catch (e: Exception) {
//                ApiResult.Error(exception = e.message.toString())
//            }
    }
}
