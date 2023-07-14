package com.ych.itunessearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ITunesItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favDao(): ItemDao

    companion object {
        // Singleton
        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "database"
                ).build()
            }
            return db!!
        }
    }
}
