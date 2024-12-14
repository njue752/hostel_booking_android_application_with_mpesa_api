package com.example.wemahostels.data

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.wemahostels.models.House
import com.example.wemahostels.models.SignupModel
import com.example.wemahostels.navigation.ROUTE_VIEW_HOUSE_ADMIN
import com.example.wemahostels.navigation.ROUTE_VIEW_USERS
//import com.example.wemahostels.navigation.ROUTE_VIEW_CLIENT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class HouseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage



    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Houses")

    // Mutable state to hold search results
    private val _searchResults = mutableStateOf<List<House>>(emptyList())
    val searchResults: State<List<House>> get() = _searchResults

    fun saveHouse(
        houseName: String,
        amenities: String,
        houseType: String,
        description: String,
        price: String,
        fee: String,
        location: String,
        navController: NavController,
        context: Context
    ) {
        val id = System.currentTimeMillis().toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("House/$id")
        val houseData = House(
            id = id,
            houseName = houseName,
            amenities = amenities,
            houseType = houseType,
            description = description,
            price = price,
            fee = fee,
            location = location,
            imageUrl = ""
        )
        dbRef.setValue(houseData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("House added successfully", context)
            } else {
                showToast("House not added successfully", context)
            }
        }
    }

    fun viewHouses(
        housesState: MutableState<List<House>>,
        context: Context
    ) {
        val ref = FirebaseDatabase.getInstance().getReference().child("House")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val houses = mutableListOf<House>()
                for (snap in snapshot.children) {
                    val value = snap.getValue(House::class.java)
                    value?.let { houses.add(it) }
                }
                housesState.value = houses // Update the state with the list of houses
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch houses", context)
            }
        })
    }

    fun updateHouse(
        context: Context,
        navController: NavController,
        houseName: String,
        amenities: String,
        houseType: String,
        description: String,
        price: String,
        fee: String,
        location: String,
        id: String
    ) {
        // Firebase reference to the "Users" node for the specific userId
        val databaseReference = FirebaseDatabase.getInstance().getReference("House/$id")

        // Create a SignupModel object with the new fields
        val updatedClient = House(
            houseName = houseName,
            amenities = amenities,
            houseType = houseType,
            description = description,
            price = price,
            fee = fee,
            location = location,
            id = id,
        )

        // Update the user data in Firebase
        databaseReference.setValue(updatedClient).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Show a success toast and navigate to the users' view
                showToast("House Updated Successfully", context)
                navController.navigate(ROUTE_VIEW_HOUSE_ADMIN)
            } else {
                // Show a failure toast
                showToast("Record update failed", context)
            }
        }
    }

    fun deleteHouse(context: Context, id: String) {
        AlertDialog.Builder(context)
            .setTitle("Delete House")
            .setMessage("Are you sure you want to delete this house?")
            .setPositiveButton("Yes") { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("House/$id")
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("House deleted successfully", context)
                    } else {
                        showToast("House deletion failed: ${task.exception?.message}", context)
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun getHouseById(id: String?, callback: (House?) -> Unit) {
        if (id.isNullOrEmpty()) {
            callback(null)
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("House/$id")
        ref.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val house = snapshot.getValue(House::class.java)
                callback(house)
            } else {
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }

    // Method to save user details (Full Name, Phone Number, Address)
    fun saveUserDetails(
        houseId: String,
        fullName: String,
        phoneNumber: String,
        address: String,
        context: Context
    ) {
        val userId = System.currentTimeMillis().toString()  // Use timestamp as user ID or other unique ID

        val userDetails = mapOf(
            "houseId" to houseId,
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "address" to address
        )

        val dbRef = FirebaseDatabase.getInstance().getReference("UserDetails/$userId")
        dbRef.setValue(userDetails).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Information saved successfully", context)
            } else {
                showToast("Failed to save information", context)
            }
        }
    }

    fun saveFeedback(
        houseId: String,
        fullName: String,
        phoneNumber: String,
        context: Context,
        feedback: String,
    ) {
        val userId = System.currentTimeMillis().toString()  // Use timestamp as user ID or other unique ID

        val userDetails = mapOf(
            "houseId" to houseId,
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "feedback" to feedback
        )

        val dbRef = FirebaseDatabase.getInstance().getReference("Feedback/$userId")
        dbRef.setValue(userDetails).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Information saved successfully", context)
            } else {
                showToast("Failed to save information", context)
            }
        }
    }

    // Function to search houses by query
    fun searchHouses(query: String, context: Context) {
        if (query.isNotEmpty()) {
            // Query Firebase Realtime Database
            val searchQuery = database.orderByChild("houseName").startAt(query).endAt(query + "\uf8ff")

            // Listen for data changes
            searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val results = mutableListOf<House>()
                    // Iterate over the results from the snapshot
                    for (houseSnapshot in snapshot.children) {
                        val house = houseSnapshot.getValue(House::class.java)
                        house?.let { results.add(it) }
                    }

                    // Update the search results
                    _searchResults.value = results

                    // Optionally show a toast with the number of results
                    Toast.makeText(context, "Found ${results.size} houses", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if something goes wrong
                    Toast.makeText(context, "Error retrieving data", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Show a toast if the query is empty
            Toast.makeText(context, "Enter a search term", Toast.LENGTH_SHORT).show()
        }
    }


    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
