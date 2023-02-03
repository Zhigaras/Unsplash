package com.zhigaras.unsplash.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zhigaras.unsplash.data.locale.LocaleRepository
import com.zhigaras.unsplash.data.locale.db.PhotoEntity
import com.zhigaras.unsplash.data.paging.PhotosPagingSource
import com.zhigaras.unsplash.data.remote.RemoteRepository
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import kotlinx.coroutines.flow.Flow
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
    
     fun loadPhotos(): Flow<PagingData<PhotoEntity>> {
        return Pager(
            config = PagingConfig(PhotosPagingSource.PAGE_SIZE),
            pagingSourceFactory = { PhotosPagingSource(remoteRepository) }
        ).flow
    }
    
}