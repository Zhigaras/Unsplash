package com.zhigaras.unsplash.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zhigaras.unsplash.data.locale.DataStoreManager
import com.zhigaras.unsplash.data.locale.db.CachedPhotoDao
import com.zhigaras.unsplash.data.locale.db.CachedPhotoDatabase
import com.zhigaras.unsplash.data.locale.db.RemoteKeysDao
import com.zhigaras.unsplash.data.paging.UnsplashRemoteMediator
import com.zhigaras.unsplash.data.remote.*
import com.zhigaras.unsplash.model.LikeResponseModel
import com.zhigaras.unsplash.model.collectionentity.CollectionEntity
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val cachedPhotoDatabase: CachedPhotoDatabase,
    private val cachedPhotoDao: CachedPhotoDao,
    private val unsplashApi: UnsplashApi,
    private val remoteKeysDao: RemoteKeysDao,
    private val dataTransformer: DataTransformer
) {
    
    suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveToken(token)
    }
    
    suspend fun checkAuthToken(): AuthCheckResult<Boolean> {
        return AuthCheckResult.Result(dataStoreManager.checkToken())
    }
    
    suspend fun getAccessToken(): String? {
        return dataStoreManager.getToken()
    }
    
    suspend fun clearDataStore() {
        dataStoreManager.clearDataStore()
    }
    
    suspend fun clearCachedPhotoDb() {
        cachedPhotoDao.clearPhotoDb()
        remoteKeysDao.clearRemoteKeys()
    }
    
    suspend fun getPhotoDetails(photoId: String): ApiResult<PhotoEntity> {
        return dataTransformer.getPhotoDetails(photoId)
    }
    
    suspend fun addToFavorites(photoId: String): Response<LikeResponseModel> {
        return unsplashApi.addToFavorites(photoId)
    }
    
    suspend fun removeFromFavorites(photoId: String): Response<LikeResponseModel> {
        return unsplashApi.removeFromFavorite(photoId)
    }
    
    suspend fun updatePhotoItem(photoEntity: PhotoEntity) {
        cachedPhotoDao.update(photoEntity)
    }
    
    @OptIn(ExperimentalPagingApi::class)
    fun loadPhotos(
        query: String? = null,
        collectionId: String? = null,
        username: String?
    ): Flow<PagingData<PhotoEntity>> {
        return Pager(
            config = PagingConfig(UnsplashRemoteMediator.PAGE_SIZE),
            remoteMediator = UnsplashRemoteMediator(
                cachedPhotoDatabase = cachedPhotoDatabase,
                cachedPhotoDao = cachedPhotoDao,
                remoteKeysDao = remoteKeysDao,
                apiRequest = getNeededCallback(query, collectionId, username)
            ),
            pagingSourceFactory = { cachedPhotoDao.showAll() }
        ).flow
    }
    
    private fun getNeededCallback(
        query: String? = null,
        collectionId: String? = null,
        username: String? = null
    ): suspend (String?, Int, Int) -> ApiResult<List<PhotoEntity>> {
        if (query != null) return { _, page, perPage ->
            dataTransformer.getSearchPhotos(query, page, perPage)
        }
        if (collectionId != null) return { _, page, perPage ->
            dataTransformer.getCollectionDetails(collectionId, page, perPage)
        }
        if (username != null) return { _, page, perPage ->
            dataTransformer.getPhotosLikedByUser(username, page, perPage)
        }
        else return { _, page, perPage ->
            dataTransformer.getFeedPhotos(null, page, perPage)
        }
    }
    
    suspend fun loadCollections(page: Int, perPage: Int): Response<List<CollectionEntity>> {
        return unsplashApi.loadCollections(page, perPage)
    }
    
    suspend fun getProfileInfo() = dataTransformer.getProfileInfo()
}