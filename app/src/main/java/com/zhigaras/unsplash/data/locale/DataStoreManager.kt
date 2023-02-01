package com.zhigaras.unsplash.data.locale

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        const val PREFERENCES_STORE_NAME = "tokens_store"
    }
    
    suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(token)] = token
        }
    }
    
    suspend fun checkToken(token: String): Boolean {
        var result = false
        dataStore.edit { prefs ->
            result = prefs.contains(booleanPreferencesKey(token))
        }
        return result
    }
    
}