package com.zhigaras.unsplash.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhigaras.unsplash.data.locale.db.PhotoEntity
import com.zhigaras.unsplash.data.remote.RemoteRepository

class PhotosPagingSource constructor(
    private val remoteRepository: RemoteRepository
) : PagingSource<Int, PhotoEntity>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoEntity>): Int = FIRST_PAGE
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoEntity> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            remoteRepository.loadPhotos(page).body()?.map { it.toPhotoEntity() }
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it!!,
                    prevKey = if (page == 1) null else page -1,
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