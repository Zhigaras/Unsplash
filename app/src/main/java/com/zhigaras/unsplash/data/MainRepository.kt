package com.zhigaras.unsplash.data

import com.zhigaras.unsplash.data.locale.LocaleRepository
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val localeRepository: LocaleRepository

) {
    
    suspend fun saveAccessToken(token: String) {
        localeRepository.saveAccessToken(token)
    }
    
}