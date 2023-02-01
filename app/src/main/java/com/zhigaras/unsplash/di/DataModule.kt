package com.zhigaras.unsplash.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zhigaras.unsplash.data.locale.DataStoreManager
import com.zhigaras.unsplash.data.remote.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    
    @Provides
    @Singleton
    fun providesPreferencesDataStore(@ApplicationContext app: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { app.preferencesDataStoreFile(DataStoreManager.PREFERENCES_STORE_NAME) }
        )
    }
    
    @Provides
    @Singleton
    fun providesRetrofit(): UnsplashApi {
        return  Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }
}