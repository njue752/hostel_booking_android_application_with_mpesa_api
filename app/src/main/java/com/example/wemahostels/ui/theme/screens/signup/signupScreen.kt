package com.example.wemahostels.ui.theme.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wemahostels.R
import com.example.wemahostels.data.AuthViewModel
import com.example.wemahostels.navigation.ROUTE_LOGIN
@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    val isLoading by authViewModel.isLoading.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    val userTypes = listOf("Client", "Admin")

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)) {  // Set the background color to magenta
        // Background Image
//        Image(
//            painter = painterResource(id = R.drawable.back),
//            contentDescription = "Background Image",
//            contentScale = ContentScale.Crop, // Adjust the scale to cover the screen
//            modifier = Modifier.fillMaxSize()
//        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp), // Add padding to avoid content being too close to edges
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register Here",
                fontSize = 20.sp,
                color = Color.Green,
                fontFamily = FontFamily.SansSerif,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Black)
                    .padding(20.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { newValue -> firstName = newValue },
                label = { Text(text = "First Name") },
                placeholder = { Text(text = "Enter your first name") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { newValue -> lastName = newValue },
                label = { Text(text = "Last Name") },
                placeholder = { Text(text = "Enter your last name") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { newValue -> phoneNumber = newValue },
                label = { Text(text = "Phone Number") },
                placeholder = { Text(text = "Enter your phone number") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { newValue -> email = newValue },
                label = { Text(text = "Email") },
                placeholder = { Text(text = "Enter your email") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { newValue -> password = newValue },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Enter your password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { newValue -> confirmPassword = newValue },
                label = { Text(text = "Confirm Password") },
                placeholder = { Text(text = "Re-enter your password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Dropdown for user type
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = userType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "User Type") },
                    placeholder = { Text(text = "Select user type") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userTypes.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                userType = type
                                expanded = false
                            },
                            text = { Text(text = type) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    authViewModel.signup(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        userType = userType, // "client" or "admin"
                        navController = navController,
                        context = context
                    )
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Green, strokeWidth = 4.dp)
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        color = Color.Green,
                        text = "Sign Up"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Clickable Text - Already have an account? Log In Here
            ClickableText(
                text = AnnotatedString("Already have an account? Log In Here"),
                onClick = { navController.navigate(ROUTE_LOGIN) }, // Adjust to your login route
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController())
}
