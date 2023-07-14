package com.ych.itunessearch

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class ITunesSearchApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setLangFromPref(this)
    }

    companion object {
        fun setLangFromPref(context: Context) {
            val langInt = SharedPref.getInstance(context.applicationContext).getValue("lang", 0)
            val locale: Locale = when (langInt) {
                2 -> {
                    Locale("zh", "CN")
                }

                1 -> {
                    Locale("zh", "HK")
                }

                else -> {
                    Locale("en", "US")
                }
            }
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        }
    }
}