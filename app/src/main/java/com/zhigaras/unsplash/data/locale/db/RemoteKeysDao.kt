package com.zhigaras.unsplash.data.locale.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)
    
    @Query("SELECT * FROM remote_keys WHERE photoId = :photoId")
    suspend fun getRemoteKeysPhotoId(photoId: String): RemoteKeys?
    
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}