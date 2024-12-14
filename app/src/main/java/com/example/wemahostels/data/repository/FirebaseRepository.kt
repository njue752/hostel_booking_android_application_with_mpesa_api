package com.example.wemahostels.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.example.wemahostels.models.SignupModel

class FirebaseRepository {

    // Firebase Realtime Database instance
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("Users") // Reference to the 'Users' node (capitalized 'U')

    // Method to get all users
    fun getUsers(onSuccess: (List<SignupModel>) -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.get()
            .addOnSuccessListener { snapshot ->
                val users = mutableListOf<SignupModel>()
                snapshot.children.forEach { childSnapshot ->
                    val user = childSnapshot.getValue(SignupModel::class.java)
                    user?.let { users.add(it) }
                }
                onSuccess(users)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    // Method to get a user by userId
    fun getUserById(userId: String, onSuccess: (SignupModel) -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(SignupModel::class.java)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure(Exception("User not found"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Method to delete a user
    fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.child(userId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
