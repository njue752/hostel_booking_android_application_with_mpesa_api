package com.example.wemahostels.ui.theme.screens.house

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.wemahostels.R
import com.example.wemahostels.data.HouseViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun AddHouseScreen(navController: NavController) {
    // State variables for user inputs
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val painter = rememberImagePainter(data = imageUri.value ?: R.drawable.placeholder)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri.value = uri
    }
    val context= LocalContext.current
    var houseName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var amenities by remember { mutableStateOf("") }
    var houseType by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") }

    // Default coordinates (Nairobi)
    var latitude by remember { mutableStateOf(-1.286389) }  // Default to Nairobi coordinates
    var longitude by remember { mutableStateOf(36.817223) }  // Default to Nairobi coordinates

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /* Navigate to Home */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                    IconButton(onClick = { /* Navigate to Settings */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { /* Call support */ }) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { /* Navigate to Profile */ }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "ADD NEW HOUSE",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(Color.Magenta)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { navController.popBackStack() }) {
                        Text(text = "BACK")
                    }
                    Button(onClick = {
                        val clientRepository=HouseViewModel()
                        clientRepository.saveHouse(
                            houseName=houseName,
                            location = location,
                            amenities = amenities,
                            houseType = houseType,
                            description = description,
                            price = price,
                            fee=fee,
                            navController=navController,
                            context=context
                        )
                    }) {
                        Text(text = "SAVE")
                    }
                }
            }

            // Image picker section
            item {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(180.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "House Image",
                        modifier = Modifier
                            .size(180.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }
                Text(text = "Attach a picture of the house", modifier = Modifier.padding(10.dp))
            }

            // Input fields for house details
            item {
                OutlinedTextField(
                    value = houseName, onValueChange = { houseName = it },
                    label = { Text(text = "House Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = amenities, onValueChange = { amenities = it },
                    label = { Text(text = "Amenities") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = houseType, onValueChange = { houseType = it },
                    label = { Text(text = "House Type") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(150.dp), // Makes the text field larger to resemble a textarea
                    maxLines = 6
                )
            }

            item {
                OutlinedTextField(
                    value = price, onValueChange = { price = it },
                    label = { Text(text = "Price") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = fee, onValueChange = { fee = it },
                    label = { Text(text = "Booking Fee") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }


            item {
                OutlinedTextField(
                    value = location, onValueChange = { location = it },
                    label = { Text(text = "Location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
            // Other input fields like location, amenities, etc.

            // Map with dynamic location selection
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(10.dp)
                ) {
                    HouseLocationMap(
                        houseName = houseName.ifEmpty { "House Location" },
                        latitude = latitude,
                        longitude = longitude,
                        onLocationSelected = { latLng ->
                            latitude = latLng.latitude
                            longitude = latLng.longitude
                            location = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}" // Optionally update location field
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun HouseLocationMap(
    houseName: String,
    latitude: Double,
    longitude: Double,
    onLocationSelected: (LatLng) -> Unit // Callback for location selection
) {
    var markerPosition by remember { mutableStateOf(LatLng(latitude, longitude)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f) // Zoom level can be adjusted
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
        onMapClick = { latLng ->
            // When the map is clicked, update the marker's position
            markerPosition = latLng
            // Notify the parent composable about the selected location
            onLocationSelected(latLng)
        }
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = houseName,
            snippet = "Coordinates: ${markerPosition.latitude}, ${markerPosition.longitude}"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AddHouseScreenPreview() {
    AddHouseScreen(rememberNavController())
}
