package com.ych.itunessearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ych.itunessearch.database.AppDatabase
import com.ych.itunessearch.http.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    val savedItemIds: LiveData<List<Int>> by lazy {
        database.favDao().getAllId()
    }

    fun addToFav(entry: ITunesDetail) {

        CoroutineScope(Dispatchers.IO).launch {
            database.favDao().insert(entry.toItem())
        }
    }

    fun removeFav(entry: ITunesDetail) {

        CoroutineScope(Dispatchers.IO).launch {
            database.favDao().delete(entry.toItem())
        }
    }

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()
    data class HomeState(
        var isLoading: Boolean = false,
        var query: String? = null,
        var items: ArrayList<ITunesDetail> = arrayListOf(),
        var lang: String = "en",
        var country: FilterAdapter.Filter? = null,
        var mediaType: FilterAdapter.Filter? = null
    )

    fun changeLang(lang: String) {
        if (lang != _uiState.value.lang) {
            _uiState.update { it.copy(lang = lang) }
            _uiState.value.query?.let {
                search(it)
            }
        }
    }

    fun search(query: String) {
        _uiState.update { it.copy(isLoading = true, query = query, items = arrayListOf()) }
        CoroutineScope(Dispatchers.IO).launch {
            val queryMap = HashMap<String, Any>()
            queryMap["term"] = query
            queryMap["lang"] = _uiState.value.lang
            if (_uiState.value.country != null) {
                queryMap["country"] = _uiState.value.country!!.getRequestValue()
            }
            if (_uiState.value.mediaType != null) {
                queryMap["media"] = _uiState.value.mediaType!!.getRequestValue()
            }
            queryMap["limit"] = 20

            val response = RetrofitClient.getApi().getPage(queryMap)

            _uiState.update {
                it.copy(isLoading = false, items = ArrayList(response.results ?: listOf()))
            }
        }
    }

    fun filter(country: FilterAdapter.Filter?, mediaType: FilterAdapter.Filter?) {
        if (!_uiState.value.query.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = true, country = country, mediaType = mediaType, items = arrayListOf()) }
            CoroutineScope(Dispatchers.IO).launch {
                val queryMap = HashMap<String, Any>()
                queryMap["term"] = _uiState.value.query!!
                queryMap["lang"] = _uiState.value.lang
                if (country != null) {
                    queryMap["country"] = country.getRequestValue()
                }
                if (mediaType != null) {
                    queryMap["media"] = mediaType.getRequestValue()
                }
                queryMap["lang"] = _uiState.value.lang
                queryMap["limit"] = 20

                val response = RetrofitClient.getApi().getPage(queryMap)

                _uiState.update {
                    it.copy(isLoading = false, items = ArrayList(response.results ?: listOf()))
                }
            }
        } else {
            _uiState.update { it.copy(country = country, mediaType = mediaType) }
        }
    }

    fun loadMore() {
        if (!_uiState.value.isLoading) {
            if (_uiState.value.query != null && _uiState.value.items.isNotEmpty()) {
                _uiState.update { it.copy(isLoading = true) }
                CoroutineScope(Dispatchers.IO).launch {
                    val queryMap = HashMap<String, Any>()
                    val size = _uiState.value.items.size
                    queryMap["term"] = _uiState.value.query ?: ""
                    queryMap["lang"] = _uiState.value.lang
                    queryMap["offset"] = size
                    queryMap["limit"] = 20

                    val response = RetrofitClient.getApi().getPage(queryMap)

                    _uiState.update {
                        it.copy(isLoading = false, items = it.items.also {
                            response.results?.let { it1 -> it.addAll(it1) }
                        })
                    }
                }
            }
        }
    }
}