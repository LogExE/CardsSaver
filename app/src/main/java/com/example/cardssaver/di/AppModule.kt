package com.example.cardssaver.di

import android.app.Application
import androidx.room.Room
import com.example.cardssaver.data.local.CardSaverDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCardSaverDatabase(app: Application): CardSaverDatabase {
        return Room.databaseBuilder(
            app,
            CardSaverDatabase::class.java,
            CardSaverDatabase.DB_NAME,
        ).build()
    }
}