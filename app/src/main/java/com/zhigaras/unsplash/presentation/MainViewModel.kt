package com.zhigaras.unsplash.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    val pagedFeedPhotos = mainRepository.loadFeedPhotos().cachedIn(viewModelScope)
    
    private val _photoDetailsFlow = MutableStateFlow<ApiResult<PhotoEntity>>(ApiResult.Loading())
    val photoDetailsFlow get() = _photoDetailsFlow.asStateFlow()
    
//    private val _likeChangingFlow = MutableStateFlow<ApiResult<LikeResponseModel>>(ApiResult.NotLoadedYet())
//    val likeChangingFlow get() = _likeChangingFlow.asStateFlow()
    
    fun getPagedSearchPhotos(query: String): Flow<PagingData<PhotoEntity>> {
        return mainRepository.loadSearchPhotos(query)
    }
    
    fun getPhotoDetails(photoId: String) {
        viewModelScope.launch(ioDispatcher) {
            _photoDetailsFlow.value = ApiResult.Loading()
            val result = mainRepository.getPhotoDetails(photoId)
            _photoDetailsFlow.value = result
            
        }
    }
    
//    fun getPhotoDetail(photoId: String): Flow<ApiResult<PhotoEntity>> {
//        val photoFlow = flow<ApiResult<PhotoEntity>> {
//            emit(ApiResult.Loading())
//            emit(mainRepository.getPhotoDetails(photoId))
//        }
//        return photoFlow
//    }
    
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