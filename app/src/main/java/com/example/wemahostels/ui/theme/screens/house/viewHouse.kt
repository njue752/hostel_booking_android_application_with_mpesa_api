package com.example.wemahostels.ui.theme.screens.house

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.wemahostels.R
import com.example.wemahostels.data.AuthViewModel
import com.example.wemahostels.data.HouseViewModel
import com.example.wemahostels.models.House
import com.example.wemahostels.navigation.ROUTE_BOOK_HOUSE
import com.example.wemahostels.navigation.ROUTE_PAY
//import com.example.wemahostels.navigation.ROUTE_VIEW_HOUSE
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberMarkerState

import androidx.compose.ui.platform.LocalContext
import com.example.wemahostels.navigation.ROUTE_HOME_ONE
import com.example.wemahostels.navigation.ROUTE_HOME_TWO
import com.example.wemahostels.navigation.ROUTE_USER_PROFILE
import com.example.wemahostels.ui.components.SocialMediaIcons

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewHouseScreen(navController: NavController, houseId: String?) {
    val houseViewModel: HouseViewModel = viewModel() // Get ViewModel
    val houseState = remember { mutableStateOf<House?>(null) }
    val context = LocalContext.current
    // Fetch house data asynchronously
    LaunchedEffect(houseId) {
        houseViewModel.getHouseById(houseId) { house ->
            houseState.value = house
        }
    }

    val house = houseState.value

    Scaffold(
        topBar = {
            // Top App Bar with Hamburger Menu, Search, Back Button, and Logout
            TopAppBar(
                title = {
                    // Search icon in the middle
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                    }
                },
                navigationIcon = {
                    // Hamburger Menu
                    IconButton(onClick = { navController.navigate(ROUTE_USER_PROFILE) }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    // Back button and Logout
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = {   val authRepository = AuthViewModel()
                        authRepository.logout(navController, context) }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Magenta)
            )
        },
        content = { innerPadding ->
            if (house != null) {
                // Show the house details when the data is loaded
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)  // Apply the padding here
                        .padding(16.dp)  // Additional padding for content
                ) {
                    item {
                        // Display static image for all houses (smaller size)
                        Image(
                            painter = rememberImagePainter(R.drawable.nyumbani),
                            contentDescription = "House Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp), // Adjust the height to make it smaller
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display house details
                        Text(text = "House Name: ${house.houseName}", fontWeight = FontWeight.Bold)
                        Text(text = "Price: ${house.price}")
                        Text(text = "Type: ${house.houseType}")
                        Text(text = "Amenities: ${house.amenities}")
                        Text(text = "Description: ${house.description}")

                        Spacer(modifier = Modifier.height(16.dp))

                        // Text for house location
                        Text(
                            text = "House Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Function to clean and parse coordinates
                        fun parseCoordinates(location: String): LatLng {
                            val parts = location.replace("Lat:", "")
                                .replace("Lng:", "")
                                .split(",")
                                .map { it.trim().toDouble() }
                            return LatLng(parts[0], parts[1])
                        }

                        // Parse the location string into LatLng
                        val houseLocation = try {
                            parseCoordinates(house.location)
                        } catch (e: Exception) {
                            LatLng(0.0, 0.0) // Default to (0,0) in case of error
                        }

                        // Google Map displaying the house's location
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(houseLocation, 15f)
                        }

                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp), // Height of the map
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = rememberMarkerState(position = houseLocation),
                                title = house.houseName
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to perform actions such as booking
                        Button(onClick = {
                            // Handle booking logic or navigate to another screen
                            navController.navigate("booked/${house.id}")
                        }) {
                            Text("Click Here to Book ")
                        }
                    }
                }
            } else {
                // Show a loading UI while data is being fetched
                Text("Loading house details...", modifier = Modifier.padding(16.dp))
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Green
            ) {
                // Home Icon
                IconButton(onClick = { navController.navigate(ROUTE_HOME_TWO) }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))

                // Add the SocialMediaIcons
                SocialMediaIcons()
            }
        }
    )
}
