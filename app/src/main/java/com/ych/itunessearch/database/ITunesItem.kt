package com.ych.itunessearch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Simplified version of ItunesDetail class. Just store the minimum into DB
 */
@Entity
data class ITunesItem(
    @PrimaryKey @ColumnInfo(name = "track_id") val trackId: Int,
    @ColumnInfo(name = "artist_name") val artistName: String?,
    @ColumnInfo(name = "artwork") val artworkUrl100: String?,
    @ColumnInfo(name = "collection_name") val collectionName: String?,
    @ColumnInfo(name = "track_name") val trackName: String?
)