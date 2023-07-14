package com.ych.itunessearch.act.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ych.itunessearch.database.AppDatabase
import com.ych.itunessearch.model.MediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    val savedItems: LiveData<List<MediaItem>> by lazy {
        database.favDao().getAll()
    }

    fun removeFav(item: MediaItem) {
        CoroutineScope(Dispatchers.IO).launch {
            database.favDao().delete(item.id)
        }
    }
}