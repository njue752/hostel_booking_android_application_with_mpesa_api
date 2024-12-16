package com.example.wemahostels.ui.theme.screens.house



import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wemahostels.R
import com.example.wemahostels.data.HouseViewModel
import com.example.wemahostels.models.House
import com.example.wemahostels.navigation.ROUTE_PAY
import com.example.wemahostels.data.AuthViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.wemahostels.navigation.ROUTE_HOME_ONE
import com.example.wemahostels.navigation.ROUTE_HOME_TWO
import com.example.wemahostels.navigation.ROUTE_USER_PROFILE
import com.example.wemahostels.ui.components.SocialMediaIcons
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailsScreen(navController: NavController, houseId: String?) {
    val houseViewModel: HouseViewModel = viewModel() // Get ViewModel
    val houseState = remember { mutableStateOf<House?>(null) }
    val context = LocalContext.current
    val navigateToPay = remember { mutableStateOf(false) }  // MutableState to trigger navigation

    // Fetch house data asynchronously
    LaunchedEffect(houseId) {
        houseViewModel.getHouseById(houseId) { house ->
            houseState.value = house
        }
    }

    val house = houseState.value

    // LaunchedEffect for delayed navigation
    LaunchedEffect(navigateToPay.value) {
        if (navigateToPay.value) {
            delay(3000)  // Wait for 3 seconds
            navController.navigate(ROUTE_PAY)  // Navigate to the payment screen
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Search icon in the middle
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
                    {IconButton(onClick = { navController.navigate(
                        ROUTE_USER_PROFILE
                    ) }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                    }
                },
                navigationIcon = {
                    // Home Icon
                    IconButton(onClick = { navController.navigate(ROUTE_HOME_TWO) }) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                    }
                },
                actions = {
                    // Back button and Logout
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = {
                        val authRepository = AuthViewModel()
                        authRepository.logout(navController, context)
                    }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Magenta)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Add heading text at the top of the screen
                Text(
                    text = "Are you interested in the house?Enter Your Details to Book",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    color = Color.Black,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                if (house != null) {
                    // Show the house ID when the data is loaded
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)  // Additional padding for content
                    ) {
                        item {
                            // House ID as label and the ID inside the text box
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(modifier = Modifier.height(8.dp))

                                // Text box with house ID
                                OutlinedTextField(
                                    value = house.id,
                                    onValueChange = {},
                                    label = { Text("House ID") },
                                    readOnly = true, // Set to true to make it non-editable
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Form fields for full name, phone number, and address
                            var fullName = remember { mutableStateOf("") }
                            var phoneNumber = remember { mutableStateOf("") }
                            var address = remember { mutableStateOf("") }

                            TextField(
                                value = fullName.value,
                                onValueChange = { fullName.value = it },
                                label = { Text("Full Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            TextField(
                                value = phoneNumber.value,
                                onValueChange = { phoneNumber.value = it },
                                label = { Text("Phone Number") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            TextField(
                                value = address.value,
                                onValueChange = { address.value = it },
                                label = { Text("Address") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Button to save information and navigate after delay
                            Button(onClick = {
                                // Save user details
                                houseViewModel.saveUserDetails(
                                    houseId = house.id,
                                    fullName = fullName.value,
                                    phoneNumber = phoneNumber.value,
                                    address = address.value,
                                    context = context
                                )

                                // Show a toast message
                                Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show()
//
//                                // Trigger the navigation after the button is clicked
//                                navigateToPay.value = true


                                // Navigate to the payment screen with houseId
                                navController.navigate("payment/${house.id}")


                            }) {
                                Text("Click Here To Book ")
                            }
                        }
                    }
                } else {
                    // Show a loading UI while data is being fetched
                    Text("Loading house details...", fontWeight = FontWeight.Bold)
                }
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
