package com.ych.itunessearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Simplified version of ItunesDetail class. Just store the minimum into DB
 */
@Entity
data class MediaItem(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "artist_name") val artistName: String?,
    @ColumnInfo(name = "artwork") val artwork: String?,
    @ColumnInfo(name = "name") val name: String?,
)