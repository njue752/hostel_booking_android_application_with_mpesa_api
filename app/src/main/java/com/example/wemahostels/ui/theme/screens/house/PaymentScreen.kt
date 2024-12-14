package com.example.wemahostels.ui.theme.screens.house

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wemahostels.R
import com.example.wemahostels.data.AuthViewModel
import com.example.wemahostels.navigation.ROUTE_BOOKED
import com.example.wemahostels.network.makePayment
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavHostController, houseId: String) { // Added houseId as a parameter
    var phoneNumber by remember { mutableStateOf("") }
    val amount = "500" // Hardcoded amount
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Search icon in the middle
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        IconButton(onClick = { /* Handle Search Action */ }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                },
                navigationIcon = {
                    // Hamburger Menu
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
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
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Green
            ) {
                // Home Icon
                IconButton(onClick = { /* Navigate to Home */ }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                // Twitter Icon
                IconButton(onClick = { /* Handle Twitter */ }) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Twitter", tint = Color.White)
                }
                // LinkedIn Icon
                IconButton(onClick = { /* Handle LinkedIn */ }) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "LinkedIn", tint = Color.White)
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Phone number input field
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                // Amount input field (hardcoded and readonly)
                OutlinedTextField(
                    value = amount,
                    onValueChange = {},
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    enabled = false, // Make this field read-only
                    modifier = Modifier.fillMaxWidth()
                )

                // Pay button
                Button(
                    onClick = {
                        if (phoneNumber.isNotEmpty()) {
                            // Trigger payment
                            makePayment(context, phoneNumber, amount)


                        } else {
                            // Handle the case when inputs are empty
                            Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Pay")
                }

                // Feedback button
                Button(
                    onClick = {
                        navController.navigate("feedback/$houseId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp) // Add some spacing between buttons
                ) {
                    Text("Give Feedback on your Experience Here")
                }
            }
        }
    )
}
