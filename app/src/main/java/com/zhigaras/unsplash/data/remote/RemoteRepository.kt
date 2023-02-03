package com.zhigaras.unsplash.data.remote

import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import com.zhigaras.unsplash.model.photoitem.PhotoItem
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val unsplashApi: UnsplashApi
) {
    
    suspend fun loadPhotos(page: Int): Response<List<PhotoItem>> {
        return unsplashApi.loadPhotos(page)
    }
    
    suspend fun getPhotoDetails(photoId: String): Response<PhotoDetails> {
        return unsplashApi.getPhotoDetails(photoId)
    }
    
}