package com.example.wemahostels.ui.theme.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wemahostels.R
import com.example.wemahostels.data.AuthViewModel
//import com.example.wemahostels.navigation.ROUTE_ADD_CLIENT
import com.example.wemahostels.navigation.ROUTE_ADD_HOUSE
import com.example.wemahostels.navigation.ROUTE_EDIT_HOUSE
import com.example.wemahostels.navigation.ROUTE_EDIT_USERS
import com.example.wemahostels.navigation.ROUTE_LOGIN
import com.example.wemahostels.navigation.ROUTE_PAY
import com.example.wemahostels.navigation.ROUTE_VIEW_HOUSE_ADMIN
import com.example.wemahostels.navigation.ROUTE_VIEW_USERS

import com.google.firebase.annotations.concurrent.Background
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "Admin"
    val userId = user?.uid ?: "" // Get the user ID from Firebase

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },  // Empty title since we don't want it in the TopAppBar
                navigationIcon = {
                    IconButton(onClick = { /* Add hamburger menu logic if needed */ }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Magenta,  // Magenta background color
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    // Middle Search icon
                    IconButton(onClick = { /* Add search logic if needed */ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                    // Right: Back and Logout icons
                    IconButton(onClick = { /* Add back logic if needed */ }) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "Back")
                    }
                    IconButton(onClick = {
                        val authRepository = AuthViewModel()
                        authRepository.logout(navController, context)
                    }
                    ) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "LogOut")
                    }
                }
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Adjusts for the BottomAppBar's height
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Dashboard background",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Column to hold the UI components
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text displaying the username below the TopAppBar
                Text(
                    text = "Admin: $userName",
                    color = Color.Magenta,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // LazyColumn to make the cards scrollable
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Add padding around the content
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Space between items
                ) {
                    item {
                        // Card for Add House
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(ROUTE_ADD_HOUSE)
                                },
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Image at the top of the card
                                Image(
                                    painter = painterResource(id = R.drawable.add),
                                    contentDescription = "Add House",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                                // Text below the image
                                Text(
                                    text = "ADD HOUSE",
                                    color = Color.Magenta,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        // Card for Edit/Delete House
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(ROUTE_VIEW_HOUSE_ADMIN)
                                },
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Image at the top of the card
                                Image(
                                    painter = painterResource(id = R.drawable.edit),
                                    contentDescription = "Edit/Delete House",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                                // Text below the image
                                Text(
                                    text = "EDIT/DELETE HOUSE",
                                    color = Color.Magenta,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        // Card for View Users
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(ROUTE_VIEW_USERS)
                                },
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Image at the top of the card
                                Image(
                                    painter = painterResource(id = R.drawable.vector),
                                    contentDescription = "View Users",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                                // Text below the image
                                Text(
                                    text = "VIEW ,EDIT AND DELETE USERS",
                                    color = Color.Magenta,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboard(rememberNavController())
}
