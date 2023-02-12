package com.zhigaras.unsplash.data.remote

import com.zhigaras.unsplash.data.locale.DataStoreManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private  val dataStoreManager: DataStoreManager) :
    Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        
        val token = runBlocking {
            dataStoreManager.getToken()
        }
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}