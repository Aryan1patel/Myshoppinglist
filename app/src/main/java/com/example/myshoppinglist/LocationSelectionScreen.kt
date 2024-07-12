package com.example.myshoppinglist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationSelectionScreen(
    location: LocationData,                        // take location data as input
    onLocationSelected: (LocationData) -> Unit){    // take onLocationSelected function as input


    // getting current latitude and longitude then setting new latitude and longitude from map
    val userLocation = remember { mutableStateOf(LatLng(location.latitude,location.longitude)) } // we make it LatLng because we need to save the latitude and longitude and get it from the onClickMap

    var cameraPositionState = rememberCameraPositionState{
        position= CameraPosition.fromLatLngZoom(userLocation.value,10f)
    }



    Column (modifier= Modifier.fillMaxSize()
        .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .background(color = Color.Black, shape = RoundedCornerShape(20.dp)),  // weight 1 means take up all available space in the column
            cameraPositionState = cameraPositionState,
            onMapClick = {
                userLocation.value=it    // save the latitude and longitude when we click on the map because Onmapclick respose as a LatLng
            }
        ){

            Marker(state = MarkerState(position = userLocation.value))        // the pointer in map when we click on the map and set marker in userlocation

        }

        var newLocation : LocationData


        Button(onClick = {
            // now when click get newlocatino from the the map when we click on map and get userlocation

            newLocation=LocationData(userLocation.value.latitude,userLocation.value.longitude)

            onLocationSelected(newLocation)
        }) {

            Text(text = "Set Location")

        }
    }



    }
