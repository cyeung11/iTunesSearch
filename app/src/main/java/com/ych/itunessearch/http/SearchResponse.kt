package com.ych.itunessearch.http

import com.ych.itunessearch.model.MediaDetail

data class SearchResponse(
    val resultCount: Int?,
    val results: List<MediaDetail>?
)