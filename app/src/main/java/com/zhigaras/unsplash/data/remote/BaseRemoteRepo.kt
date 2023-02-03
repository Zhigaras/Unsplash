package com.zhigaras.unsplash.data.remote

import android.content.res.Resources
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.locale.db.PhotoEntity
import com.zhigaras.unsplash.model.photoitem.PhotoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRemoteRepo (
    private val ioDispatcher: CoroutineDispatcher
) {
    
    suspend fun safeApiCall(apiToBeCalled: suspend () -> Response<List<PhotoItem>>): ApiResult<List<PhotoEntity>> {
        return withContext(ioDispatcher) {
            try {
                val response: Response<List<PhotoItem>> = apiToBeCalled()
                
                if (response.isSuccessful) {
                    ApiResult.Success(_data = response.body()?.map { it.toPhotoEntity() })
                } else {
                    if (response.code() == 404) {
                        ApiResult.Error(
                            exception = Resources.getSystem().getString(R.string.not_found_404),
                            _needToGoBack = true,
//                            _errorButtonText = "Back"
                        )
                    } else {
                        ApiResult.Error(exception = response.message())
                    }
                }
            } catch (e: HttpException) {
                ApiResult.Error(
                    exception = e.message ?: Resources.getSystem()
                        .getString(R.string.something_went_wrong)
                )
            } catch (e: IOException) {
                ApiResult.Error(Resources.getSystem().getString(R.string.check_connection))
            } catch (e: Exception) {
                ApiResult.Error(exception = Resources.getSystem()
                    .getString(R.string.something_went_wrong))
            }
        }
    }
}