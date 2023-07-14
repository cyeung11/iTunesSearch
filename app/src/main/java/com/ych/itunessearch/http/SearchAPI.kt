package com.ych.itunessearch.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SearchAPI {
    @GET("search")
    fun getPage(@QueryMap params: HashMap<String, Any>) : Call<SearchResponse>
}