package com.zhigaras.unsplash.data.remote

import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import com.zhigaras.unsplash.model.photoitem.PhotoItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {
    
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
    }
    
    @GET("photos")
    suspend fun loadFeedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<PhotoItem>>
    
    @GET("photos/{id}")
    suspend fun getPhotoDetails(
        @Path("id") photoId: String
    ): Response<PhotoDetails>
    
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<PhotoItem>>
    
    @POST("photos/{id}/like")
    suspend fun addToFavorites(
        @Path("id") photoId: String
    ): Response<PhotoDetails>
    
    @DELETE("photos/{id}/like")
    suspend fun removeFromFavorite(
        @Path("id") photoId: String
    ): Response<PhotoDetails>
    
}