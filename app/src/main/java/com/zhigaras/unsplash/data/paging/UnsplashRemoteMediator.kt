package com.zhigaras.unsplash.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.zhigaras.unsplash.model.photoitem.PhotoItem

@OptIn(ExperimentalPagingApi::class)
class UnsplashRemoteMediator: RemoteMediator<Int, PhotoItem>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoItem>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
    
}