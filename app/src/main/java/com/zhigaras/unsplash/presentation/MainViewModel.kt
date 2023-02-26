package com.zhigaras.unsplash.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.data.paging.CollectionPagingSource
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.collectionentity.CollectionEntity
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import com.zhigaras.unsplash.model.photoentity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val collectionPagingSource: CollectionPagingSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val ioScope = CoroutineScope(viewModelScope.coroutineContext + ioDispatcher)
    
    private val _selectedCollectionFlow = MutableStateFlow<CollectionEntity?>(null)
    val selectedCollectionFlow get() = _selectedCollectionFlow
    
    val pagedCollections: Flow<PagingData<CollectionEntity>> = Pager(
        config = PagingConfig(CollectionPagingSource.PAGE_SIZE),
        pagingSourceFactory = { collectionPagingSource }
    ).flow.cachedIn(ioScope)
    
    private val _photoDetailsFlow = MutableStateFlow<ApiResult<PhotoEntity>>(ApiResult.Loading())
    val photoDetailsFlow get() = _photoDetailsFlow.asStateFlow()
    
    private val _profileInfoFlow = MutableStateFlow<ApiResult<User>>(ApiResult.Loading())
    val profileInfoFlow get() = _profileInfoFlow
    
    private val _errorFlow = Channel<String>()
    val errorFlow get() = _errorFlow.receiveAsFlow()
    
    fun getPagedPhotos(
        query: String? = null,
        collectionId: String? = null,
        username: String? = null
    ): Flow<PagingData<PhotoEntity>> {
        return mainRepository.loadPhotos(
            query = query,
            collectionId = collectionId,
            username = username
        )
    }
    
    fun getPhotoDetails(photoId: String) {
        ioScope.launch {
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
        Log.d("AAA", "like clicked")
        ioScope.launch {
            kotlin.runCatching {
                if (isLiked) mainRepository.removeFromFavorites(photoId)
                else mainRepository.addToFavorites(photoId)
            }.onSuccess { response ->
                Log.d("AAA success", response.toString())
                response.body()?.let { mainRepository.updatePhotoItem(it.photo) }
            }.onFailure {
                Log.d("AAA failure", it.message.toString())
                _errorFlow.send(it.message ?: "")
            }
        }
    }
    
    fun getProfileInfo() {
        _profileInfoFlow.value = ApiResult.Loading()
        ioScope.launch {
            val result = mainRepository.getProfileInfo()
            _profileInfoFlow.value = result
        }
    }
}