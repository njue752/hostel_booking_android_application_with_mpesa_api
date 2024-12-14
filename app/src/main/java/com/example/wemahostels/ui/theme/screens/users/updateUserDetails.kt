package com.example.wemahostels.ui.theme.screens.users

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wemahostels.data.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wemahostels.models.SignupModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser

@Composable
fun UpdateProfileScreen(
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
    var password by remember { mutableStateOf("") } // To store new password

    // Fetch the current user's details when the screen is launched
    LaunchedEffect(userId) {
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
            enabled = true // Editable now
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true // Editable now
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true // Editable now
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true // Editable now
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            enabled = true // Editable now
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
                updateUserProfile(context, userId, firstName, lastName, phoneNumber, email, password) {
                    // Navigate back to the profile screen after successful update
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Update Profile")
        }
    }
}

// Function to update user details in Firebase
fun updateUserProfile(
    context: Context,
    userId: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    email: String,
    password: String,
    onUpdateComplete: () -> Unit
) {
    val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

    // Update user details in the Firebase Realtime Database
    val userUpdates = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "phoneNumber" to phoneNumber,
        "email" to email,
        "userType" to "User" // You can modify userType if needed
    )
    userRef.updateChildren(userUpdates).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // If password is provided, update it in Firebase Authentication
            if (password.isNotEmpty()) {
                FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                    ?.addOnCompleteListener { passwordUpdateTask ->
                        if (passwordUpdateTask.isSuccessful) {
                            showUpdateToast(context, "Password updated successfully")
                        } else {
                            showUpdateToast(context, "Password update failed: ${passwordUpdateTask.exception?.message}")
                        }
                    }
            }

            // Notify that the user details were updated successfully
            showUpdateToast(context, "Profile updated successfully")
            onUpdateComplete() // Callback to navigate back
        } else {
            showUpdateToast(context, "Failed to update profile: ${task.exception?.message}")
        }
    }
}

// Helper function to show a toast message (renamed to avoid ambiguity)
fun showUpdateToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
