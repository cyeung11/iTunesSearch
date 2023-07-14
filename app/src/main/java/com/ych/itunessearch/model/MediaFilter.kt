package com.ych.itunessearch.model

import android.content.Context
import android.util.Log
import com.ych.itunessearch.adapter.FilterAdapter
import com.ych.itunessearch.R
import java.util.Locale

data class MediaFilter(val text: String, val value: String) {

    // UI Display
    fun getDisplayText(): String {
        return text
    }

    // For network call
    fun getRequestValue(): String {
        return value
    }

    companion object {
        fun getTypeFilter(context: Context): List<MediaFilter> {
            return listOf(
                MediaFilter(context.getString(R.string.media_type_movie), "movie"),
                MediaFilter(context.getString(R.string.media_type_tv), "tvShow"),
                MediaFilter(context.getString(R.string.media_type_music), "music"),
                MediaFilter(context.getString(R.string.media_type_audio_book), "audiobook"),
                MediaFilter(context.getString(R.string.media_type_podcast), "podcast"),
            )
        }

        fun getCountryFilters(context: Context): List<MediaFilter> {
            val locale = context.resources.configuration.locale
            return Locale.getISOCountries().map {
                MediaFilter(Locale("", it).getDisplayCountry(locale), it)
            }
        }
    }
}