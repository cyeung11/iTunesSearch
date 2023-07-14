package com.ych.itunessearch.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("SELECT * FROM itunesitem")
    fun getAll(): LiveData<List<ITunesItem>>

    @Query("SELECT track_id FROM itunesitem")
    fun getAllId(): LiveData<List<Int>>

    @Insert
    fun insert(item: ITunesItem)

    @Delete
    fun delete(item: ITunesItem)
}
