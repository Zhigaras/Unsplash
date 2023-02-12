package com.zhigaras.unsplash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.di.IoDispatcher
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    val pagedPhotos = mainRepository.loadPhotos().cachedIn(viewModelScope)
    
    private val _photoDetailsFlow = MutableStateFlow<ApiResult<PhotoDetails>>(ApiResult.Loading())
    val photoDetailsFlow get() = _photoDetailsFlow.asStateFlow()
    
    private val _likeChangingFlow = MutableStateFlow<ApiResult<PhotoDetails>>(ApiResult.NotLoadedYet())
    val likeChangingFlow get() = _likeChangingFlow.asStateFlow()
    
    fun getPhotoDetail(photoId: String) {
        viewModelScope.launch(ioDispatcher) {
            _photoDetailsFlow.value = ApiResult.Loading()
            val result = mainRepository.getPhotoDetails(photoId)
            _photoDetailsFlow.value = result
        }
    }
    
    fun onLikeClick(isLiked: Boolean, photoId: String) {
        viewModelScope.launch {
            _likeChangingFlow.value = ApiResult.Loading()
            val result =
                if (isLiked) mainRepository.removeFromFavorites(photoId)
                else mainRepository.addToFavorites(photoId)
            _likeChangingFlow.value = result
        }
    }
}