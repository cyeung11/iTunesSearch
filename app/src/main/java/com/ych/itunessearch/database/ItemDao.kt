package com.ych.itunessearch.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ych.itunessearch.model.MediaItem

@Dao
interface ItemDao {
    @Query("SELECT * FROM mediaitem")
    fun getAll(): LiveData<List<MediaItem>>

    @Query("SELECT id FROM mediaitem")
    fun getAllId(): LiveData<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MediaItem)

    @Query("DELETE FROM mediaitem where id = :id")
    fun delete(id: Int)
}
