import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.wemahostels.R
import com.example.wemahostels.data.HouseViewModel
import com.example.wemahostels.models.House
import com.example.wemahostels.navigation.ROUTE_HOME_ONE
import com.example.wemahostels.navigation.ROUTE_HOME_TWO
import com.example.wemahostels.ui.components.SocialMediaIcons
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

// House Data Class
data class House(
    var houseName: String = "",
    var amenities: String = "",
    var houseType: String = "",
    var description: String = "",
    var price: String = "",
    var fee: String = "",
    var location: String = "",
    var id: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun UpdateHouseScreen(navController: NavHostController, id: String) {
    val focusRequester = remember { FocusRequester() }
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val painter = rememberImagePainter(data = imageUri.value ?: R.drawable.placeholder)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    var houseData by remember { mutableStateOf(House()) }
    val context = LocalContext.current

    // Load house data from Firebase
    val houseViewModel: HouseViewModel = viewModel()
    DisposableEffect(Unit) {
        houseViewModel.getHouseById(id) { house ->
            house?.let {
                houseData = it
            }
        }
        onDispose { }
    }

    // State for selected location coordinates
    var selectedLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update House Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Magenta)
            )
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
        },
        content = { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Update and Delete House Details",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .background(Color.Magenta)
                            .padding(10.dp)
                    )
                }

                // Image Picker Section
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            shape = CircleShape,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(180.dp)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(180.dp)
                                    .clickable { launcher.launch("image/*") },
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(text = "Update picture")
                    }
                }

                // Editable fields
                item {
                    OutlinedTextField(
                        value = houseData.houseName,
                        onValueChange = { houseData = houseData.copy(houseName = it) },
                        label = { Text("House Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = houseData.amenities,
                        onValueChange = { houseData = houseData.copy(amenities = it) },
                        label = { Text("Amenities") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = houseData.description,
                        onValueChange = { houseData = houseData.copy(description = it) },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = houseData.price,
                        onValueChange = { houseData = houseData.copy(price = it) },
                        label = { Text("Price") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = houseData.fee,
                        onValueChange = { houseData = houseData.copy(fee = it) },
                        label = { Text("Fee") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                // Location TextField
                item {
                    OutlinedTextField(
                        value = houseData.location,
                        onValueChange = { houseData = houseData.copy(location = it) },
                        label = { Text("Location") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

// Add Spacer inside an item block
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

// House Location and Google Map
                item {
                    Text(
                        text = "House Location",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Camera position state
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(selectedLocation, 15f)
                    }

                    // Marker state to remember the position
                    val markerState = rememberMarkerState(position = selectedLocation)

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            selectedLocation = latLng
                            houseData = houseData.copy(location = "${latLng.latitude},${latLng.longitude}")
                            markerState.position = latLng // Update the marker position when the map is clicked
                        }
                    ) {
                        // Add a marker at the selected location
                        Marker(
                            state = markerState,
                            title = houseData.houseName,
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                        )
                    }
                }


                // Buttons for navigation
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { navController.popBackStack() }) {
                            Text(text = "Back")
                        }
                        Button(onClick = {
                            houseViewModel.updateHouse(
                                context = context,
                                navController = navController,
                                houseName = houseData.houseName,
                                amenities = houseData.amenities,
                                houseType = houseData.houseType,
                                description = houseData.description,
                                price = houseData.price,
                                fee = houseData.fee,
                                location = houseData.location,
                                id = houseData.id
                            )
                        }) {
                            Text(text = "Update")
                        }
                    }
                }
            }

        }
    )
}



@Preview
@Composable
fun UpdateHouseScreenPreview() {
    val navController = rememberNavController()
    UpdateHouseScreen(navController = navController, id = "exampleId")
}
