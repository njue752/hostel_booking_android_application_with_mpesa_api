package com.example.wemahostels.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.wemahostels.models.SignupModel
import com.example.wemahostels.navigation.ROUTE_HOME_ONE
import com.example.wemahostels.navigation.ROUTE_HOME_TWO
import com.example.wemahostels.navigation.ROUTE_LOGIN
import com.example.wemahostels.navigation.ROUTE_VIEW_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun signup(
        firstName: String, lastName: String, phoneNumber: String,
        email: String, password: String, confirmPassword: String,
        userType: String, navController: NavController, context: Context
    ) {
        // Check if all fields are filled
        if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank() ||
            email.isBlank() || password.isBlank() || confirmPassword.isBlank() || userType.isBlank()) {
            showToast("Please fill all the fields", context)
            return
        }

        // Validate if passwords match
        if (password != confirmPassword) {
            showToast("Passwords do not match", context)
            return
        }

        // Show loading indicator
        _isLoading.value = true

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _isLoading.value = false

            if (task.isSuccessful) {
                // Get the user ID
                val userId = mAuth.currentUser?.uid ?: ""

                // Create a user model for saving to the database
                val userData = SignupModel(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    userType = userType,
                    userId = userId,
                    userName = firstName + " " + lastName // Use full name as the userName
                )

                // Save user data to the Firebase Realtime Database
                saveUserToDatabase(userId, userData, navController, context)

                // Update user profile with display name
                val user = mAuth.currentUser
                val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(userData.userName)
                    .build()

                user?.updateProfile(profile)?.addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        showToast("Display name set correctly", context)
                    } else {
                        showToast("Failed to set display name", context)
                    }
                }
            } else {
                _errorMessage.value = task.exception?.message
                showToast(task.exception?.message ?: "Registration failed", context)
            }
        }
    }

    fun saveUserToDatabase(
        userId: String, userData: SignupModel, navController: NavController, context: Context
    ) {
        val regRef = FirebaseDatabase.getInstance().getReference("Users/$userId")
        regRef.setValue(userData).addOnCompleteListener { regTask ->
            if (regTask.isSuccessful) {
                showToast("User Successfully Registered", context)
                navController.navigate(ROUTE_LOGIN)
            } else {
                _errorMessage.value = regTask.exception?.message
                showToast(regTask.exception?.message ?: "Database error", context)
            }
        }
    }

    // Helper function to show toast messages
    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }




    fun viewUsers(
        clients: SnapshotStateList<SignupModel>,
        context: Context
    ): SnapshotStateList<SignupModel> {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clients.clear()
                for (snap in snapshot.children) {
                    val value = snap.getValue(SignupModel::class.java)
                    if (value != null) {
                        clients.add(value)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch users: ${error.message}", context)
            }
        })
        return clients
    }


    fun updateClient(
        context: Context,
        navController: NavController,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        password: String,
        confirmPassword: String,
        userType: String,
        userId: String
    ) {
        // Firebase reference to the "Users" node for the specific userId
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users/$userId")

        // Create a SignupModel object with the new fields
        val updatedClient = SignupModel(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            userType = userType,
            userId = userId,
            userName = "$firstName $lastName" // Full name as the userName
        )

        // Update the user data in Firebase
        databaseReference.setValue(updatedClient).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Show a success toast and navigate to the users' view
                showToast("Client Updated Successfully", context)
                navController.navigate(ROUTE_VIEW_USERS)
            } else {
                // Show a failure toast
                showToast("Record update failed", context)
            }
        }
    }


    fun deleteClient(context: Context, userId: String) {
        AlertDialog.Builder(context)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete this user?")
            .setPositiveButton("Yes") { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("Users/$userId")
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("User deleted successfully", context)
                    } else {
                        showToast("User deletion failed: ${task.exception?.message}", context)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }






    fun login(email: String, password: String, navController: NavController, context: Context) {
        if (email.isBlank() || password.isBlank()) {
            showToast("Email and password are required", context)
            return
        }

        _isLoading.value = true

        // Authenticate the user with email and password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _isLoading.value = false

            if (task.isSuccessful) {
                val userId = mAuth.currentUser?.uid ?: ""
                // Fetch the user's data from the Firebase database
                val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

                userRef.get().addOnCompleteListener { dataTask ->
                    if (dataTask.isSuccessful && dataTask.result.exists()) {
                        val userType = dataTask.result.child("userType").value.toString()

                        // Navigate to respective dashboard based on userType
                        when (userType) {
                            "Client" -> {
                                showToast("Welcome, Client!", context)
                                navController.navigate(ROUTE_HOME_TWO)
                            }
                            "Admin" -> {
                                showToast("Welcome, Admin!", context)
                                navController.navigate(ROUTE_HOME_ONE)
                            }
                            else -> {
                                showToast("User type not recognized", context)
                            }
                        }
                    } else {
                        showToast("Failed to retrieve user data", context)
                    }
                }.addOnFailureListener {
                    showToast("Database error: ${it.message}", context)
                }
            } else {
                _errorMessage.value = task.exception?.message
                showToast(task.exception?.message ?: "Login Failed", context)
            }
        }
    }fun getUserDetails(userId: String, context: Context) {
    // Reference to the user's data in the Firebase Realtime Database
    val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

    userRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val userSnapshot = task.result
            if (userSnapshot.exists()) {
                // Retrieve user data
                val firstName = userSnapshot.child("firstName").value.toString()
                val lastName = userSnapshot.child("lastName").value.toString()
                val phoneNumber = userSnapshot.child("phoneNumber").value.toString()
                val email = userSnapshot.child("email").value.toString()
                val userType = userSnapshot.child("userType").value.toString()

                // You can use the retrieved data as needed, for example:
                showToast("User Details: $firstName $lastName", context)

                // Here, you could update your UI with these values (e.g., through a LiveData or StateFlow)
            } else {
                showToast("User not found in database", context)
            }
        } else {
            showToast("Failed to retrieve user details: ${task.exception?.message}", context)
        }
    }
}
    // In AuthViewModel
    fun getUserPhoneNumber(userId: String, callback: (String) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")
        userRef.get().addOnSuccessListener { snapshot ->
            val phoneNumber = snapshot.child("phoneNumber").value.toString()
            callback(phoneNumber)
        }.addOnFailureListener {
            callback("") // Handle failure to get phone number
        }
    }


    fun viewUserDetails(
        context: Context,
        navController: NavController
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return showToast("User not logged in", context)

        // Reference to the logged-in user's data in Firebase Realtime Database
        val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")

        // Fetch the user's data
        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userSnapshot = task.result
                if (userSnapshot.exists()) {
                    // Retrieve user data
                    val firstName = userSnapshot.child("firstName").value.toString()
                    val lastName = userSnapshot.child("lastName").value.toString()
                    val phoneNumber = userSnapshot.child("phoneNumber").value.toString()
                    val email = userSnapshot.child("email").value.toString()
                    val userType = userSnapshot.child("userType").value.toString()



                    // Optionally, navigate to the user's profile page where they can view and update their details
                    // navController.navigate("ROUTE_USER_PROFILE")
                } else {
                    showToast("User not found in database", context)
                }
            } else {
                showToast("Failed to retrieve user details: ${task.exception?.message}", context)
            }
        }
    }


    fun updateClientDetails(
        context: Context,
        navController: NavController,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String,
        password: String,
        confirmPassword: String,
        userType: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return showToast("User not logged in", context)

        // Firebase reference to the "Users" node for the specific userId
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users/$userId")

        // Create a SignupModel object with the new fields
        val updatedClient = SignupModel(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            userType = userType,
            userId = userId,
            userName = "$firstName $lastName" // Full name as the userName
        )

        // Update the user data in Firebase
        databaseReference.setValue(updatedClient).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Show a success toast and navigate to the users' view
                showToast("Client Updated Successfully", context)
                navController.navigate(ROUTE_VIEW_USERS)
            } else {
                // Show a failure toast
                showToast("Record update failed", context)
            }
        }
    }





    fun logout(navController: NavController, context: Context) {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
        navController.navigate(ROUTE_LOGIN)
    }


}



