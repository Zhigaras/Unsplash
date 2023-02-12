package com.zhigaras.unsplash.data.locale

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val PREFERENCES_STORE_NAME = "tokens_store"
        const val AUTH_TOKEN_KEY = "auth_token_key"
    }
    
    suspend fun saveToken(token: String) {
        Log.d("AAA save token", token)
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(AUTH_TOKEN_KEY)] = token
        }
        var log = mapOf<Preferences.Key<*>, Any>()
        dataStore.edit {
            log = it.asMap()
            
        }
        Log.d("AAA prefs", log.toString())
    }
    
    suspend fun checkToken(): Boolean {
        return dataStore.edit {}.contains(stringPreferencesKey(AUTH_TOKEN_KEY))
    }
    
    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
            
        }
        var log = mapOf<Preferences.Key<*>, Any>()
        dataStore.edit {
            log = it.asMap()
        
        }
        Log.d("AAA cleared prefs", log.toString())
    }
}