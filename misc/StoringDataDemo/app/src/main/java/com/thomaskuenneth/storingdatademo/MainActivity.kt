package com.thomaskuenneth.storingdatademo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore("user_preferences")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val prefs = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val current = prefs.getBoolean("hasBeenSet", false)
        println(current)
        prefs.edit().putBoolean("hasBeenSet", !current).apply()

        val key = booleanPreferencesKey("hasBeenSet")
        val flow: Flow<Boolean> = dataStore.data
            .map { currentPreferences ->
                currentPreferences[key] ?: false
            }
        lifecycleScope.launch {
            println(flow.first())
            dataStore.edit { settings ->
                val currentCounterValue = settings[key] ?: false
                settings[key] = !currentCounterValue
            }
        }
    }
}