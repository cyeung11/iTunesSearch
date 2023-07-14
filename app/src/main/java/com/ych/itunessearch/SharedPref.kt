package com.ych.itunessearch

import android.content.Context
import android.content.SharedPreferences

class SharedPref(mContext: Context) {
    private val preferences: SharedPreferences = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE)

    fun setValue(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    fun getValue(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }

    fun setValue(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getValue(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    companion object {
        private const val TAG = "SharedPref"
        private var instance: SharedPref? = null

        fun getInstance(context: Context): SharedPref {
            if (null == instance) {
                instance = SharedPref(context.applicationContext)
            }
            return instance!!
        }
    }

}
