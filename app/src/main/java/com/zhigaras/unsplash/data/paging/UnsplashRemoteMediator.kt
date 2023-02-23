package com.zhigaras.unsplash.data.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.zhigaras.unsplash.data.locale.db.*
import com.zhigaras.unsplash.data.remote.UnsplashApi
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UnsplashRemoteMediator @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val cachedPhotoDatabase: CachedPhotoDatabase,
    private val cachedPhotoDao: CachedPhotoDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val query: String?
) : RemoteMediator<Int, PhotoEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: FIRST_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                nextKey
            }
        }
        
        try {
            val photos = if (query == null){
                unsplashApi.loadFeedPhotos(page = page, perPage =  PAGE_SIZE).body()
            } else {
                unsplashApi.loadSearchPhoto(query = query, page = page, perPage = PAGE_SIZE).body()?.results
            }
            try {
                checkNotNull(photos)
            } catch (exception: java.lang.IllegalStateException) {
                return MediatorResult.Error(exception)
            }
            val endOfPaginationReached = photos.isEmpty()
            cachedPhotoDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    cachedPhotoDao.clearPhotoDb()
                }
                val prevKey = if (page == FIRST_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos.map {
                    RemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeysDao.insertAll(keys)
                cachedPhotoDao.insertAll(photos)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
    
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { photoEntity ->
                cachedPhotoDatabase.getRemoteKeysDao().getRemoteKeysPhotoId(photoEntity.id)
            }
    }
    
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PhotoEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photoEntity ->
                cachedPhotoDatabase.getRemoteKeysDao().getRemoteKeysPhotoId(photoEntity.id)
            }
    }
    
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PhotoEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                cachedPhotoDatabase.getRemoteKeysDao().getRemoteKeysPhotoId(photoId)
            }
        }
    }
    
    companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 10
    }
}