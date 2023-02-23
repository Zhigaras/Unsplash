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
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.LikeResponseModel
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val cachedPhotoDatabase: CachedPhotoDatabase,
    private val cachedPhotoDao: CachedPhotoDao,
    private val unsplashApi: UnsplashApi,
    private val remoteKeysDao: RemoteKeysDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): BaseRemoteRepo(ioDispatcher = ioDispatcher) {
    
    suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveToken(token)
    }
    
    suspend fun checkAuthToken(): AuthCheckResult<Boolean> {
        return AuthCheckResult.Result(dataStoreManager.checkToken())
    }
    
    suspend fun clearDataStore() {
        dataStoreManager.clearDataStore()
    }
    
    suspend fun clearCachedPhotoDb() {
        cachedPhotoDao.clearPhotoDb()
        remoteKeysDao.clearRemoteKeys()
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
    
    suspend fun updatePhotoItem(photoEntity: PhotoEntity) {
        cachedPhotoDao.update(photoEntity)
    }
    
    @OptIn(ExperimentalPagingApi::class)
    fun loadFeedPhotos(): Flow<PagingData<PhotoEntity>> {
        return Pager(
            config = PagingConfig(UnsplashRemoteMediator.PAGE_SIZE),
            remoteMediator = UnsplashRemoteMediator(
                unsplashApi = unsplashApi,
                cachedPhotoDatabase = cachedPhotoDatabase,
                cachedPhotoDao = cachedPhotoDao,
                remoteKeysDao = remoteKeysDao,
                query = null
                ),
            pagingSourceFactory = { cachedPhotoDao.showAll() }
        ).flow
    }
    
    @OptIn(ExperimentalPagingApi::class)
    fun loadSearchPhotos(query: String): Flow<PagingData<PhotoEntity>> {
        return Pager(
            config = PagingConfig(UnsplashRemoteMediator.PAGE_SIZE),
            remoteMediator = UnsplashRemoteMediator(
                unsplashApi = unsplashApi,
                cachedPhotoDatabase = cachedPhotoDatabase,
                cachedPhotoDao = cachedPhotoDao,
                remoteKeysDao = remoteKeysDao,
                query = query
            ),
            pagingSourceFactory = { cachedPhotoDao.showAll() }
        ).flow
    }
    
}