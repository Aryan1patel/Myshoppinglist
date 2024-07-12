package com.example.myshoppinglist

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

data class GeocodingResponse(                      // from api response
        val results : List<GeocodingResult>,
        val status : String,         // This corresponds to the "status" field in the JSON response
        val isSuccessful: Boolean
        )

data class GeocodingResult(
    val formatted_address: String  // only getting formatted_address from the api response
)
