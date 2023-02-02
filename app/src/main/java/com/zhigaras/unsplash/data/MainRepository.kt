package com.zhigaras.unsplash.data

import com.zhigaras.unsplash.data.locale.LocaleRepository
import com.zhigaras.unsplash.data.remote.RemoteRepository
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val localeRepository: LocaleRepository,
    private val remoteRepository: RemoteRepository
) {
    
    suspend fun saveAccessToken(token: String) {
        localeRepository.saveAccessToken(token)
    }
    
    suspend fun getPhotoDetails(photoId: String): Response<PhotoDetails> {
        return remoteRepository.getPhotoDetails(photoId)
    }
    
//    suspend fun loadPhotos(): Response<List<PhotoModel>> {
//        return remoteRepository.loadPhotos()
//    }
    
}