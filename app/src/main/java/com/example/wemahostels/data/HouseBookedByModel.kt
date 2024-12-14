package com.example.wemahostels.data



import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wemahostels.models.House
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HouseDetailsModel : ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

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

    fun getHouseById(id: String?, callback: (House?) -> Unit) {
        if (id.isNullOrEmpty()) {
            callback(null)  // Return null if id is empty
            return
        }

        // Reference to the specific house node using the id
        val ref = FirebaseDatabase.getInstance().getReference("House/$id")

        // Get the data from Firebase
        ref.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Convert the snapshot to a House object
                val house = snapshot.getValue(House::class.java)

                // Call the callback function with the house object
                callback(house)
            } else {
                // Return null if the house doesn't exist
                callback(null)
            }
        }.addOnFailureListener {
            // Return null in case of failure
            callback(null)
        }
    }



    // Method to show toast messages
    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

