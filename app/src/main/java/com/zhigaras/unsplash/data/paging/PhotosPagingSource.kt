package com.zhigaras.unsplash.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhigaras.unsplash.data.remote.RemoteRepository
import com.zhigaras.unsplash.model.photo.PhotoModel
import javax.inject.Inject

class PhotosPagingSource @Inject constructor(
    private val remoteRepository: RemoteRepository
) : PagingSource<Int, PhotoModel>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoModel>): Int = FIRST_PAGE
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            remoteRepository.loadPhotos(page).body()!!
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
    
    companion object {
        private const val FIRST_PAGE = 1
        const val PAGE_SIZE = 10
    }
}