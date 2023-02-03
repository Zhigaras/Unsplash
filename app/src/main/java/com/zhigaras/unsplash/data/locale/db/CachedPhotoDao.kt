package com.zhigaras.unsplash.data.locale.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CachedPhotoDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)
    
    @Query("SELECT * FROM cached_photo")
    fun showAll(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM cached_photo")
    suspend fun clearPhotoDb()
}