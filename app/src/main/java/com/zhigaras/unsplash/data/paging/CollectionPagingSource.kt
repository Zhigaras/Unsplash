package com.zhigaras.unsplash.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.model.collectionentity.CollectionEntity
import javax.inject.Inject

class CollectionPagingSource @Inject constructor(
    private val mainRepository: MainRepository
) : PagingSource<Int, CollectionEntity>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionEntity>): Int = FIRST_PAGE
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionEntity> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            mainRepository.loadCollections(page, PAGE_SIZE).body()!!
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