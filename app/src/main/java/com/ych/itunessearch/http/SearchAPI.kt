package com.ych.itunessearch.http

import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SearchAPI {
    @GET("search")
    suspend fun getPage(@QueryMap params: HashMap<String, Any>) : SearchResponse
}