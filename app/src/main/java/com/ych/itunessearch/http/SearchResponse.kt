package com.ych.itunessearch.http

import com.ych.itunessearch.ITunesDetail

data class SearchResponse(
    val resultCount: Int?,
    val results: List<ITunesDetail>?
)