package com.ych.itunessearch.http

import com.ych.itunessearch.Entity

data class SearchResponse(
    val resultCount: Int?,
    val results: List<Entity>?
)