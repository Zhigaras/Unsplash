package com.zhigaras.unsplash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    
    val pagedPhotos = mainRepository.loadPhotos().cachedIn(viewModelScope)
    
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