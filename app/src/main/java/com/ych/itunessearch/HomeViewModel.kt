package com.ych.itunessearch

import androidx.lifecycle.ViewModel
import com.ych.itunessearch.http.RetrofitClient
import com.ych.itunessearch.http.SearchResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    data class HomeState(
        var isLoading: Boolean = false,
        var query: String? = null,
        var response: SearchResponse? = null
    )

    fun search(query: String) {
        _uiState.update { it.copy(isLoading = true) }
        CoroutineScope(Dispatchers.IO).launch {
            val queryMap = HashMap<String, String>()
            queryMap["term"] = query

            val response = RetrofitClient.getApi().getPage(queryMap)

            _uiState.update {
               HomeState(false, query, response)
            }
        }
    }
}