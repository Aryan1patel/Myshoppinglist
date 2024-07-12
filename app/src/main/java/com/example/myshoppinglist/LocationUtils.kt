package com.example.myshoppinglist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task


class LocationUtils(val context : Context) {                 // it requires the context because to request permission on location updata

    private val _fusedLocationClient :  FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")// ignore the error
    fun requestLocationUpdate(viewModel: LocationViewModel) {  // get lat and long from device             // update location and use viewmodel to store location

        if (!hasLocationPermission(context)) {
            Log.e("LocationUtils", "Location permission not granted")
            return
        }
        viewModel.startLoadingLocation()                                 // for loading value to update
                                                                                            //        Log.e("LocationUtils", " location permission granted")

                                                        //        checkLocationSettings { isenabled -                         // for gps we direct use in navigation
                                                        //
                                                        //            if (isenabled) {
                                                        //                Log.e("LocationUtils", " innnnn")
        val locationCallback = object : LocationCallback() {                  // LocationCallBack is predefined
            override fun onLocationResult(locationResult: LocationResult) {   // locationResult is also predefined method(that's why we overriding) which get lat and long
                           //super.onLocationResult(locationResult)
                                                                                   Log.d("LocationUtils", "Location callback triggered")
                locationResult.lastLocation?.let {    // get last location mean user location
                    val newLocation =
                        LocationData(                                  // getting location data latitude and logitude from api and storing in data class
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                                                                       //updating location
                    viewModel.updateLocation(newLocation)              // passing the value in viewmodel

                } ?: run {
                    Log.e("LocationUtils", "Location result was null")
                }

                _fusedLocationClient.removeLocationUpdates(this)              //crucial for stopping location updates when they are no longer needed
            }
            // for log only
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                Log.d("LocationUtils", "Location availability changed: $locationAvailability")
            }
        }


        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()
        // made this before in above private val _fusedLocationClient :  FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        // now pass these to receive periodic updates about the device's location
        _fusedLocationClient.requestLocationUpdates(   // in Android it is responsible for requesting location updates from the Fused Location Provider Client in Android
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
                                                                                                                                        //            }else {
                                            //                Log.e("LocationUtils", "GPS is not enabled")
                                            //                Toast.makeText(context, "Please enable GPS", Toast.LENGTH_SHORT).show()
                                            //            }
                                            //        }

    fun hasLocationPermission(context: Context): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationSettings(callback: (Boolean) -> Unit) {                 // for gps

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // GPS is enabled
            Log.d("LocationUtils", "GPS is enabled")
            callback(true)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context as Activity, 1001)         // pop up or dialog to enable gps
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("LocationUtils", "Error starting resolution for GPS: ${sendEx.message}")
                    callback(false)
                }
            } else {
                Log.e("LocationUtils", "GPS is not enabled")
                callback(false)
            }
        }
    }

}