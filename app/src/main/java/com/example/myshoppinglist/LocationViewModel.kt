package com.example.myshoppinglist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {


    private val _location = mutableStateOf<LocationData?>(null)   // making private mutable of type locaiton data
    val location : State<LocationData?> = _location     // making public

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address :State<List<GeocodingResult>> = _address

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun startLoadingLocation() {
        _isLoading.value = true           // for loading screen
    }

    fun updateLocation(newlocation : LocationData){  // function to update location
        Log.d("LocationViewModel", "Location updated: $newlocation")
        _location.value= newlocation
        _isLoading.value=false
    }

    fun fetchAddress(latLng: String) {       // api and retrofit use to featch address from latitude and longitude
        Log.d("LocationViewModel", "Fetching started")

        viewModelScope.launch {
            try {
                val result = RetrofitCilent.create().getAddressFromCoordinate(      // get readable address
                    latLng,                                          // it get string
                    "AIzaSyAu1fsy_Kh5uCeyaVHr0tTofPEXZ9O5RJE" // Replace with your actual API key here
                )
                //Log.d("LocationViewModel", "Request: ${result}")        // Log request and response
                // Check if the status is OK before updating the live data

                _address.value = result.results

//                if (result.status == "OK") {
//                    _address.value = result.results
//                   // Log.d("LocationViewModel", "Address fetched successfully: ${result.results}")
//                } else {
//                    Log.d("LocationViewModel", "Geocoding API returned status: ${result.status}")
//                }

            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error fetching address: ${e.message}", e)
            }
        }
    }




}
