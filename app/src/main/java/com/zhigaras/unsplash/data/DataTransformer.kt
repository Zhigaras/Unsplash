package com.zhigaras.unsplash.data

import android.content.Context
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.data.remote.BaseRemoteRepo
import com.zhigaras.unsplash.data.remote.UnsplashApi
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DataTransformer @Inject constructor(
    private val unsplashApi: UnsplashApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : BaseRemoteRepo(ioDispatcher = ioDispatcher, context) {
    
    suspend fun getSearchPhotos(
        query: String,
        page: Int,
        perPage: Int
    ): ApiResult<List<PhotoEntity>> {
        val response = safeApiCall { unsplashApi.loadSearchPhoto(query, page, perPage) }
        return if (response is ApiResult.Success) ApiResult.Success(response.data?.results)
        else response as ApiResult.Error
    }
    
    suspend fun getFeedPhotos(
        query: String?,
        page: Int,
        perPage: Int
    ): ApiResult<List<PhotoEntity>> {
        return safeApiCall { unsplashApi.loadFeedPhotos(page, perPage) }
    }
    
    suspend fun getCollectionDetails(
        collectionId: String,
        page: Int,
        perPage: Int
    ): ApiResult<List<PhotoEntity>> {
        return safeApiCall { unsplashApi.loadCollectionDetails(collectionId, page, perPage) }
    }
    
    suspend fun getPhotoDetails(photoId: String): ApiResult<PhotoEntity> {
        return safeApiCall { unsplashApi.getPhotoDetails(photoId) }
    }
    
    suspend fun getProfileInfo() = safeApiCall { unsplashApi.getProfileInfo() }
    
    suspend fun getPhotosLikedByUser(username: String, page: Int, perPage: Int) =
        safeApiCall { unsplashApi.getPhotosLikedByUser(username, page, perPage) }
}