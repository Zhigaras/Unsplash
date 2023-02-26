package com.zhigaras.unsplash.data

import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.data.remote.BaseRemoteRepo
import com.zhigaras.unsplash.data.remote.UnsplashApi
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.LikeResponseModel
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DataTransformer @Inject constructor(
    private val unsplashApi: UnsplashApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseRemoteRepo(ioDispatcher = ioDispatcher) {
    
    suspend fun getSearchPhotos(query: String, page: Int, perPage: Int): ApiResult<List<PhotoEntity>> {
        val response = safeApiCall { unsplashApi.loadSearchPhoto(query, page, perPage) }
        return if (response is ApiResult.Success) ApiResult.Success(response.data?.results)
        else response as ApiResult.Error
    }
    
    suspend fun getFeedPhotos(query: String?,page: Int, perPage: Int): ApiResult<List<PhotoEntity>> {
        return safeApiCall { unsplashApi.loadFeedPhotos(page, perPage) }
    }
    
    suspend fun getCollectionDetails(collectionId: String, page: Int, perPage: Int): ApiResult<List<PhotoEntity>> {
        return safeApiCall { unsplashApi.loadCollectionDetails(collectionId, page, perPage) }
    }
    
    suspend fun getPhotoDetails(photoId: String): ApiResult<PhotoEntity> {
        return safeApiCall { unsplashApi.getPhotoDetails(photoId) }
    }
    
    suspend fun addToFavorites(photoId: String): ApiResult<LikeResponseModel> {
        return safeApiCall { unsplashApi.addToFavorites(photoId) }
    }
    
    suspend fun removeFromFavorites(photoId: String): ApiResult<LikeResponseModel> {
        return safeApiCall { unsplashApi.removeFromFavorite(photoId) }
    }
}