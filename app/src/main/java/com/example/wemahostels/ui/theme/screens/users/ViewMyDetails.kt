package com.example.wemahostels.ui.theme.screens.users

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wemahostels.data.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wemahostels.models.SignupModel
import com.example.wemahostels.navigation.ROUTE_PAY
import com.example.wemahostels.navigation.ROUTE_UPDATE_USER_PROFILE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun UserProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current  // Get the context
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    // Declare mutable states for user details
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }

    // Fetch the current user's details when the screen is launched
    LaunchedEffect(userId) {
        // Pass both context and userId to fetchUserDetails
        fetchUserDetails(context, userId) { userDetails ->
            firstName = userDetails.firstName
            lastName = userDetails.lastName
            phoneNumber = userDetails.phoneNumber
            email = userDetails.email
            userType = userDetails.userType
        }
    }

    // User Profile UI Layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "User Profile",
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display user details in text fields
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Display, not editable
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Display, not editable
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Display, not editable
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Display, not editable
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display user type
        OutlinedTextField(
            value = userType,
            onValueChange = { userType = it },
            label = { Text("User Type") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Display, not editable
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to allow the user to update their profile
        Button(
            onClick = {
                navController.navigate(ROUTE_UPDATE_USER_PROFILE )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Profile")
        }

    }
}

// Fetch user details from Firebase
fun fetchUserDetails(context: Context, userId: String, onUserDetailsFetched: (UserDetails) -> Unit) {
    val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

    // Fetch user data from Firebase
    userRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val userSnapshot = task.result
            if (userSnapshot.exists()) {
                val firstName = userSnapshot.child("firstName").value.toString()
                val lastName = userSnapshot.child("lastName").value.toString()
                val phoneNumber = userSnapshot.child("phoneNumber").value.toString()
                val email = userSnapshot.child("email").value.toString()
                val userType = userSnapshot.child("userType").value.toString()

                // Pass the retrieved data to the UI
                onUserDetailsFetched(UserDetails(firstName, lastName, phoneNumber, email, userType))
            } else {
                // Handle case when user is not found
                showToast(context, "User not found in database")
            }
        } else {
            // Handle failed request
            showToast(context, "Failed to retrieve user details: ${task.exception?.message}")
        }
    }
}

// Helper function to show a toast message
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

// User details data class
data class UserDetails(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val userType: String
)

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    // Provide the context and NavController here for preview
    UserProfileScreen(navController = NavController(LocalContext.current))
}
