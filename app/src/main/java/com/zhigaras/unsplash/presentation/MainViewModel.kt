package com.zhigaras.unsplash.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.data.paging.CollectionPagingSource
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.collectionentity.CollectionEntity
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val collectionPagingSource: CollectionPagingSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val _selectedCollectionFlow = MutableStateFlow<CollectionEntity?>(null)
    val selectedCollectionFlow get() = _selectedCollectionFlow
    
    val pagedCollections: Flow<PagingData<CollectionEntity>> = Pager(
        config = PagingConfig(CollectionPagingSource.PAGE_SIZE),
        pagingSourceFactory = { collectionPagingSource }
    ).flow.cachedIn(viewModelScope)
    
    private val _photoDetailsFlow = MutableStateFlow<ApiResult<PhotoEntity>>(ApiResult.Loading())
    val photoDetailsFlow get() = _photoDetailsFlow.asStateFlow()

//    private val _likeChangingFlow = MutableStateFlow<ApiResult<LikeResponseModel>>(ApiResult.NotLoadedYet())
//    val likeChangingFlow get() = _likeChangingFlow.asStateFlow()
    
    fun getPagedPhotos(
        query: String? = null,
        collectionId: String? = null
    ): Flow<PagingData<PhotoEntity>> {
        return mainRepository.loadPhotos(query = query, collectionId = collectionId)
    }
    
    fun getPhotoDetails(photoId: String) {
        viewModelScope.launch(ioDispatcher) {
            _photoDetailsFlow.value = ApiResult.Loading()
            val result = mainRepository.getPhotoDetails(photoId)
            _photoDetailsFlow.value = result
            
        }
    }
    
    fun saveSelectedCollection(collection: CollectionEntity) {
        Log.d("AAA collection saved", collection.collectionId)
        _selectedCollectionFlow.value = collection
    }
    
    fun onLikeClick(isLiked: Boolean, photoId: String) {
        Log.d("AAA", "clicked")
//        _likeChangingFlow.value = ApiResult.Loading()
        viewModelScope.launch(ioDispatcher) {
            val result =
                if (isLiked) mainRepository.removeFromFavorites(photoId)
                else mainRepository.addToFavorites(photoId)
            result.data?.photo?.let { mainRepository.updatePhotoItem(it) }
            Log.d("AAA result", result.toString())
//            _likeChangingFlow.value = result
        }
    }
}