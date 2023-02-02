package com.zhigaras.unsplash.data.remote

import com.zhigaras.unsplash.model.photo.PhotoModel
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val unsplashApi: UnsplashApi
) {
    
    suspend fun loadPhotos(page: Int): Response<List<PhotoModel>> {
        return unsplashApi.loadPhotos(page)
    }
    
    suspend fun getPhotoDetails(photoId: String): Response<PhotoDetails> {
        return unsplashApi.getPhotoDetails(photoId)
    }
    
}