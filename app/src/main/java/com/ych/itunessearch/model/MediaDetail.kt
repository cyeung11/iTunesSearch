package com.ych.itunessearch.model

data class MediaDetail(
    val artistId: Int,
    val artistName: String?,
    val artistViewUrl: String?,
    val artworkUrl100: String?,
    val artworkUrl30: String?,
    val artworkUrl60: String?,
    val collectionCensoredName: String?,
    val collectionExplicitness: String?,
    val collectionId: Int,
    val collectionName: String?,
    val collectionPrice: Double,
    val collectionViewUrl: String?,
    val country: String?,
    val currency: String?,
    val discCount: Int,
    val discNumber: Int,
    val isStreamable: Boolean,
    val kind: String?,
    val previewUrl: String?,
    val primaryGenreName: String?,
    val releaseDate: String?,
    val trackCensoredName: String?,
    val trackCount: Int,
    val trackExplicitness: String?,
    val trackId: Int, // this could be 0 for some item. Use collectionId instead if so
    val trackName: String?,
    val trackNumber: Int,
    val trackPrice: Double,
    val trackTimeMillis: Int,
    val trackViewUrl: String?,
    val wrapperType: String?
) {
    fun toItem(): MediaItem {
        return MediaItem(
            artistName = artistName,
            artwork = artworkUrl100,
            name = name,
            id = id
        )
    }

    val name: String?
        get() =  if (trackName.isNullOrBlank()) collectionName else trackName
    val id: Int
        get() = if (trackId != 0) trackId else collectionId
}