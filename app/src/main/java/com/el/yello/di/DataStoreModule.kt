package com.el.yello.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.el.yello.BuildConfig
import com.example.data.local.qualifier.App
import com.example.data.local.qualifier.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import java.security.GeneralSecurityException
import java.security.KeyStore

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val APP_PREFERENCES_NAME = "APP_DATA"

    @Provides
    @Singleton
    @User
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = if (BuildConfig.DEBUG) {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    } else {
        try {
            createEncryptedSharedPreferences(context.packageName, context)
        } catch (e: GeneralSecurityException) {
            deleteMasterKeyEntry()
            deleteExistingPref(context.packageName, context)
            createEncryptedSharedPreferences(context.packageName, context)
        }
    }

    @Provides
    @Singleton
    @App
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = if (BuildConfig.DEBUG) {
        context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
    } else {
        try {
            createEncryptedSharedPreferences(APP_PREFERENCES_NAME, context)
        } catch (e: GeneralSecurityException) {
            deleteMasterKeyEntry()
            deleteExistingPref(APP_PREFERENCES_NAME, context)
            createEncryptedSharedPreferences(APP_PREFERENCES_NAME, context)
        }
    }

    private fun deleteExistingPref(fileName: String, context: Context) {
        context.deleteSharedPreferences(fileName)
    }

    private fun deleteMasterKeyEntry() {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
            deleteEntry(MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        }
    }

    private fun createEncryptedSharedPreferences(
        fileName: String,
        context: Context
    ): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            fileName,
            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}