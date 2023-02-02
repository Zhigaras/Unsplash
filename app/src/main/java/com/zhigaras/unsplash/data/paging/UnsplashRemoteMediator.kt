package com.zhigaras.unsplash.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.zhigaras.unsplash.model.photo.PhotoModel

@OptIn(ExperimentalPagingApi::class)
class UnsplashRemoteMediator: RemoteMediator<Int, PhotoModel>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoModel>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
    
}