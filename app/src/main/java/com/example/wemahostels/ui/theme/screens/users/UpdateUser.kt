

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.request.Disposable
import com.example.wemahostels.data.AuthViewModel
//import com.example.wemahostels.models.User
import com.google.firebase.firestore.auth.User


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.wemahostels.R
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter



import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.wemahostels.navigation.ROUTE_HOME_ONE
import com.example.wemahostels.navigation.ROUTE_HOME_TWO
import com.example.wemahostels.navigation.ROUTE_USER_PROFILE
import com.example.wemahostels.ui.components.SocialMediaIcons

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

data class SignupModel(
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    val userType: String = "",
    var userId: String = ""
)

val SignupModelSaver: Saver<SignupModel, *> = run {
    mapSaver(
        save = {
            mapOf(
                "firstName" to it.firstName,
                "lastName" to it.lastName,
                "phoneNumber" to it.phoneNumber,
                "email" to it.email,
                "password" to it.password,
                "confirmPassword" to it.confirmPassword,
                "userType" to it.userType,
                "userId" to it.userId
            )
        },
        restore = {
            SignupModel(
                firstName = it["firstName"] as String,
                lastName = it["lastName"] as String,
                phoneNumber = it["phoneNumber"] as String,
                email = it["email"] as String,
                password = it["password"] as String,
                confirmPassword = it["confirmPassword"] as String,
                userType = it["userType"] as String,
                userId = it["userId"] as String
            )
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateClientScreen(navController: NavHostController, id: String) {
    val focusRequester = remember { FocusRequester() }
    val imageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val painter = rememberImagePainter(data = imageUri.value ?: R.drawable.placeholder, builder = { crossfade(true) })
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri.value = it }
    }

    var clientData by rememberSaveable(stateSaver = SignupModelSaver) { mutableStateOf(SignupModel()) }
    val context = LocalContext.current

    // Load client data from Firebase
    val currentDataRef = FirebaseDatabase.getInstance().getReference().child("Users/$id")
    DisposableEffect(Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val client = snapshot.getValue(SignupModel::class.java)
                client?.let {
                    clientData = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        }
        currentDataRef.addValueEventListener(listener)
        onDispose { currentDataRef.removeEventListener(listener) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_USER_PROFILE) }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                actions = {

                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    IconButton(onClick = { /* Handle Logout Click */ }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                },
                modifier = Modifier.background(Color.Magenta)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Green
            ) {
                // Home Icon
                IconButton(onClick = { navController.navigate(ROUTE_HOME_TWO) }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))

                // Add the SocialMediaIcons
                SocialMediaIcons()
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "UPDATE AND DELETE USERS DETAILS",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.Magenta)
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { }) {
                    Text(text = "BACK")
                }
                Button(onClick = {
                    val authViewModel = AuthViewModel()
                    authViewModel.updateClient(
                        context = context,
                        navController = navController,
                        firstName = clientData.firstName,
                        lastName = clientData.lastName,
                        phoneNumber = clientData.phoneNumber,
                        email = clientData.email,
                        password = clientData.password,
                        confirmPassword = clientData.confirmPassword,
                        userType = clientData.userType,
                        userId = id
                    )
                }) {
                    Text(text = "UPDATE")
                }
            }

            // Image Picker
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(180.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(180.dp)
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }
                Text(text = "Update picture")
            }

            // TextFields for each field
            OutlinedTextField(
                value = clientData.firstName,
                onValueChange = { clientData = clientData.copy(firstName = it) },
                placeholder = { Text(text = "Enter First Name") },
                label = { Text(text = "Enter First Name") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = clientData.lastName,
                onValueChange = { clientData = clientData.copy(lastName = it) },
                placeholder = { Text(text = "Enter Last Name") },
                label = { Text(text = "Enter Last Name") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = clientData.phoneNumber,
                onValueChange = { clientData = clientData.copy(phoneNumber = it) },
                placeholder = { Text(text = "Enter your Phone Number") },
                label = { Text(text = "Enter your Phone Number") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = clientData.email,
                onValueChange = { clientData = clientData.copy(email = it) },
                placeholder = { Text(text = "Enter your Email") },
                label = { Text(text = "Enter your Email") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = clientData.password,
                onValueChange = { clientData.password = it },
                placeholder = { Text(text = "Enter your Password") },
                label = { Text(text = "Enter your Password") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = clientData.confirmPassword,
                onValueChange = { clientData.confirmPassword = it },
                placeholder = { Text(text = "Confirm Password") },
                label = { Text(text = "Confirm Password") },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
fun UpdateClientScreenPreview() {
    val navController = rememberNavController()
    UpdateClientScreen(navController = navController, id = "exampleId")
}