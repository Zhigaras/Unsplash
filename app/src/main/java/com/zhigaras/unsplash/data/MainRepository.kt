package com.zhigaras.unsplash.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zhigaras.unsplash.data.locale.DataStoreManager
import com.zhigaras.unsplash.data.locale.db.CachedPhotoDao
import com.zhigaras.unsplash.data.locale.db.CachedPhotoDatabase
import com.zhigaras.unsplash.data.locale.db.PhotoEntity
import com.zhigaras.unsplash.data.locale.db.RemoteKeysDao
import com.zhigaras.unsplash.data.paging.UnsplashRemoteMediator
import com.zhigaras.unsplash.data.remote.UnsplashApi
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val cachedPhotoDatabase: CachedPhotoDatabase,
    private val cachedPhotoDao: CachedPhotoDao,
    private val unsplashApi: UnsplashApi,
    private val remoteKeysDao: RemoteKeysDao
) {
    
    suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveToken(token)
    }
    
    suspend fun getPhotoDetails(photoId: String): Response<PhotoDetails> {
        return unsplashApi.getPhotoDetails(photoId)
    }
    
    @OptIn(ExperimentalPagingApi::class)
    fun loadPhotos(): Flow<PagingData<PhotoEntity>> {
        return Pager(
            config = PagingConfig(UnsplashRemoteMediator.PAGE_SIZE),
            remoteMediator = UnsplashRemoteMediator(
                unsplashApi = unsplashApi,
                cachedPhotoDatabase = cachedPhotoDatabase,
                cachedPhotoDao = cachedPhotoDao,
                remoteKeysDao = remoteKeysDao
                ),
            pagingSourceFactory = { cachedPhotoDao.showAll() }
        ).flow
    }
    
}