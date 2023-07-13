package com.ych.itunessearch.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val instance : Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://itunes.apple.com/")
            .build()
         }

    fun getApi(): SearchAPI {
        return instance.create(SearchAPI::class.java)
    }

}