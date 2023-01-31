package com.zhigaras.unsplash.data.locale

import javax.inject.Inject

class LocaleRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    
    suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveToken(token)
    }
    
}
