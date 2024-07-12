package com.example.myshoppinglist

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitCilent {

    private const val Base_Url ="https://maps.googleapis.com/"

    fun create(): GeocodingApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl(Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(GeocodingApiService::class.java)
    }
}