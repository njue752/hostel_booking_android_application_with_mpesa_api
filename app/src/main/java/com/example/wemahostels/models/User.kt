package com.example.wemahostels.models

data class SignupModel(
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    val userType: String = "",
    var userId: String = "",
    val userName: String = "" // Make sure this has a default value
) {
    // No-argument constructor generated automatically due to @JvmOverloads
}
