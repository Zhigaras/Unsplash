package com.zhigaras.unsplash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.data.paging.PhotosPagingSource
import com.zhigaras.unsplash.model.photo.PhotoModel
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val photosPagingSource: PhotosPagingSource
) : ViewModel() {
    
    val pagedPhotos: Flow<PagingData<PhotoModel>> = Pager(
        config = PagingConfig(PhotosPagingSource.PAGE_SIZE),
        pagingSourceFactory = { photosPagingSource }
    ).flow.cachedIn(viewModelScope)
    
    private val _photoDetailsFlow = MutableStateFlow<PhotoDetails?>(null)
    val photoDetailsFlow get() = _photoDetailsFlow.asStateFlow()
    
    fun getPhotoDetail(photoId: String) {
        viewModelScope.launch {
            val result = mainRepository.getPhotoDetails(photoId)
            _photoDetailsFlow.value = result.body()
        }
    }
    
    
//    fun loadPhotos() {
//        viewModelScope.launch {
//            val result = mainRepository.loadPhotos()
//
//        }
//    }
    
}