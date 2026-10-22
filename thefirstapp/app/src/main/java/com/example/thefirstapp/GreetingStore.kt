package com.example.thefirstapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class GreetingStore(private val context: Context) {

    private val greetingKey = stringPreferencesKey("greeting_text")

    val greetingText: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[greetingKey] ?: DEFAULT_GREETING
    }

    suspend fun saveGreetingText(text: String) {
        context.dataStore.edit { preferences ->
            preferences[greetingKey] = text
        }
    }

    companion object {
        const val DEFAULT_GREETING = "Hello Android!"
    }
}
