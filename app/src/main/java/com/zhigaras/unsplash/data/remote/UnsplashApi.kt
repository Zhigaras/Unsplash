package com.zhigaras.unsplash.data.remote

import com.zhigaras.unsplash.domain.AppAuth
import com.zhigaras.unsplash.model.photoitem.PhotoItem
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {
    
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
    }
    
    @Headers("Authorization: Client-ID ${AppAuth.AuthConfig.CLIENT_ID}")
    @GET("photos")
    suspend fun loadFeedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<PhotoItem>>
    
    @Headers("Authorization: Client-ID ${AppAuth.AuthConfig.CLIENT_ID}")
    @GET("photos/{id}")
    suspend fun getPhotoDetails(
        @Path("id") photoId: String
    ): Response<PhotoDetails>
    
    @Headers("Authorization: Client-ID ${AppAuth.AuthConfig.CLIENT_ID}")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<PhotoItem>>
    
}