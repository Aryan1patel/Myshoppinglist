package com.example.myshoppinglist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.myshoppinglist.ui.theme.MyshoppinglistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyshoppinglistTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                   Navigation(navname = navhostData())


                }
            }
        }
    }
}

@Composable
fun Navigation(navname:navhostData){

    val navController = rememberNavController()
    val viewmodel : LocationViewModel = viewModel()
    val context= LocalContext.current
    val locationUtils = LocationUtils(context)



    NavHost(navController = navController, startDestination = navname.shoppingscreen) {

        composable(navname.shoppingscreen){
            ShoppinglistApp(
                locationUtils = locationUtils,
                viewModel = viewmodel,
                navController = navController,
                context = context,
                address = viewmodel.address.value.firstOrNull()?.formatted_address ?: "No address",       // fristornull mean in list no matter how many item in your list i want frist item
                                                                     // elvish operatore if no address
            )
        }



        dialog(navname.locationscreen){backstack->    // naming it to backstack


            if(viewmodel.isLoading.value ){         // all value updates in viewmodel this is for loading screen
                Column (modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    CircularProgressIndicator(
                        color=Color.Green,
                        modifier = Modifier.background(
                            color = Color.Black,
                            shape = RoundedCornerShape(20.dp))
                    )

                }

            }
            Log.d("Locationnavigation for mapsScreen","upper")

            viewmodel.location.value?.let {it1->
                Log.d("Locationnavigation or mapsScreen","in location screen")

                LocationSelectionScreen(location = it1, onLocationSelected = {
                    viewmodel.fetchAddress("${it.latitude},${it.longitude}")
                    navController.popBackStack()
                })


            }


        }
    }
}














@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyshoppinglistTheme {

      //  ShoppinglistApp()

    }
}