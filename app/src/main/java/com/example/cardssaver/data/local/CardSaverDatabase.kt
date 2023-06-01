package com.example.cardssaver.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cardssaver.data.local.dao.CardDao
import com.example.cardssaver.data.local.entity.CardEntity

@Database(version = 1, entities = [
    CardEntity::class
])
abstract class CardSaverDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        const val DB_NAME = "CardSaver_DB"
        @Volatile private var instance: CardSaverDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            val ins = Room.databaseBuilder(
                context,
                CardSaverDatabase::class.java,
                DB_NAME
            ).build()
            instance = ins

            ins
        }
    }
}