package com.example.myshoppinglist

import retrofit2.http.GET
import retrofit2.http.Query

// it passed latlng and key to link to get api

interface GeocodingApiService {
                                                  // we have to pass in this link
    @GET("maps/api/geocode/json")                 // api starting address from this https://maps.googleapis.com/maps/api/geocode/json?latlng=22.6068,80.3752&key=AIzaSyAu1fsy_Kh5uCeyaVHr0tTofPEXZ9O5RJE
    suspend fun getAddressFromCoordinate(
        @Query("latlng") latlng :String,          // pass these in viewmodel
        @Query("key") apiKey :String
    ) : GeocodingResponse                     // return in this response from json file


}