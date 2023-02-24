package com.zhigaras.unsplash.data.remote

import com.zhigaras.unsplash.model.LikeResponseModel
import com.zhigaras.unsplash.model.SearchResponseModel
import com.zhigaras.unsplash.model.collectionentity.CollectionEntity
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
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
    ): Response<List<PhotoEntity>>
    
    @GET("photos/{id}")
    suspend fun getPhotoDetails(
        @Path("id") photoId: String
    ): Response<PhotoEntity>
    
    @GET("search/photos")
    suspend fun loadSearchPhoto(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<SearchResponseModel>
    
    @POST("photos/{id}/like")
    suspend fun addToFavorites(
        @Path("id") photoId: String
    ): Response<LikeResponseModel>
    
    @DELETE("photos/{id}/like")
    suspend fun removeFromFavorite(
        @Path("id") photoId: String
    ): Response<LikeResponseModel>
    
    @GET("collections")
    suspend fun loadCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<CollectionEntity>>
    
    @GET("collections/{id}/photos")
    suspend fun getCollectionDetails(
        @Path("id") collectionId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<PhotoEntity>>
}