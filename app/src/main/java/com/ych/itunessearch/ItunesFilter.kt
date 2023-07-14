package com.ych.itunessearch

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import java.util.Locale

data class ItunesFilter(val text: String, val value: String): FilterAdapter.Filter {

    override fun getDisplayText(): String {
        return text
    }

    override fun getRequestValue(): String {
        return value
    }

    companion object {
        fun getTypeFilter(context: Context): List<ItunesFilter> {
            return listOf<ItunesFilter>(
                ItunesFilter(context.getString(R.string.media_type_movie), "movie"),
                ItunesFilter(context.getString(R.string.media_type_tv), "tvShow"),
                ItunesFilter(context.getString(R.string.media_type_music), "music"),
                ItunesFilter(context.getString(R.string.media_type_audio_book), "audiobook"),
                ItunesFilter(context.getString(R.string.media_type_podcast), "podcast"),
            )
        }

        fun getCountryFilters(context: Context): List<ItunesFilter> {
            val locale = context.resources.configuration.locale
            return Locale.getISOCountries().map {
                Log.d("filter", it + Locale("", it).displayName)
                ItunesFilter(Locale("", it).getDisplayCountry(locale), it)
            }
        }
    }
}