package com.zhigaras.unsplash.data.remote

import android.content.Context
import android.util.Log
import com.zhigaras.unsplash.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRemoteRepo(
    private val ioDispatcher: CoroutineDispatcher,
    private val context: Context
) {
    
    suspend fun <Data> safeApiCall(apiToBeCalled: suspend () -> Response<Data>): ApiResult<Data> {
        return withContext(ioDispatcher) {
            try {
            val response = apiToBeCalled()
            Log.d("AAA response", response.isSuccessful.toString())
            
            if (response.isSuccessful) {
                ApiResult.Success(_data = response.body())
            } else if (response.code() == 403) {
                ApiResult.Error(exception = context.getString(R.string.rate_limit_exceeded))
            } else {
                ApiResult.Error(exception = context.getString(R.string.check_connection))
            }

            } catch (e: HttpException) {
                ApiResult.Error(
                    exception = e.message
                        ?: context.getString(R.string.something_went_wrong)
                )
            } catch (e: IOException) {
                ApiResult.Error(context.getString(R.string.check_connection))
            } catch (e: Exception) {
                ApiResult.Error(exception = e.message.toString())
            }
        }
    }
}
