package com.zhigaras.unsplash.data.locale.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhotoEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class CachedPhotoDatabase : RoomDatabase() {
    
    abstract fun getPhotoDao(): CachedPhotoDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
    
    companion object {
        const val DATABASE_NAME = "cached_photo_database"
    }
    
}