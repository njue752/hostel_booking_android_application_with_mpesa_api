import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.wemahostels.R
import com.example.wemahostels.models.House
//import com.example.wemahostels.navigation.ROUTE_VIEW_HOUSE
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.wemahostels.data.AuthViewModel
import com.example.wemahostels.data.HouseViewModel
import com.example.wemahostels.navigation.ROUTE_HOME_ONE
import com.example.wemahostels.navigation.ROUTE_HOME_TWO
import com.example.wemahostels.navigation.ROUTE_PAY
import com.example.wemahostels.navigation.ROUTE_USER_PROFILE
import com.example.wemahostels.ui.components.SocialMediaIcons

import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDashboard(navController: NavController, houseViewModel: HouseViewModel) {
    val housesState = remember { mutableStateOf<List<House>>(emptyList()) }
    val context = LocalContext.current

    // Fetch houses from Firebase when the screen is first loaded
    LaunchedEffect(true) {
        houseViewModel.viewHouses(housesState, context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "House List",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {          navController.navigate(ROUTE_USER_PROFILE) }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    // Search Icon in the center

                    // Back Button
                    IconButton(onClick = { navController.popBackStack()  }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    // Log Out
                    IconButton(onClick = {   val authRepository = AuthViewModel()
                        authRepository.logout(navController, context)}) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "LogOut")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Magenta,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
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
        ,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Marquee Text with Green Background
                MarqueeText(text = "Welcome to Wema Hostels!", backgroundColor = Color.Green)

                // Carousel and other content
                Carousel()

                // LazyColumn containing LazyRows with two cards per row
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(housesState.value.chunked(2)) { housePair ->
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            housePair.forEach { house ->
                                item {
                                    HouseItem(
                                        house = house,
                                        onViewMoreDetailsClick = {
                                            navController.navigate("viewhouse/${house.id}")

                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}



@Composable
fun HouseItem(
    house: House,
    onViewMoreDetailsClick: () -> Unit
) {
    val imageResId = if (house.imageUrl.isNotEmpty()) {
        R.drawable.nyumbani// Replace with your actual image logic
    } else {
        R.drawable.nyumbani // Placeholder
    }

    // Function to clean and parse coordinates
    fun parseCoordinates(location: String): com.google.android.gms.maps.model.LatLng {
        val parts = location.replace("Lat:", "")
            .replace("Lng:", "")
            .split(",")
            .map { it.trim().toDouble() }
        return com.google.android.gms.maps.model.LatLng(parts[0], parts[1])
    }

    // Parse the location string into LatLng
    val houseLocation = try {
        parseCoordinates(house.location)
    } catch (e: Exception) {
        com.google.android.gms.maps.model.LatLng(0.0, 0.0) // Default to (0,0) in case of error
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .width(200.dp)
                .height(350.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = Color.Magenta)
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                val painter = painterResource(id = imageResId)
                Image(
                    painter = painter,
                    contentDescription = "House Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )

                Box(modifier = Modifier.fillMaxHeight()) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // House Name
                        Text(
                            text = "House Name: ${house.houseName}",
                            color = Color.Blue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Price
                        Text(
                            text = "House Price: ${house.price}",
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Booking Fee
                        Text(
                            text = "Booking Fee: ${house.fee}",
                            color = Color.Blue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // House Type
                        Text(
                            text = "House Type: ${house.houseType}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Amenities
                        Text(
                            text = "Amenities: ${house.amenities}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Description
                        Text(
                            text = "House Description: ${house.description}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Google Map Section
                        val cameraPositionState = rememberCameraPositionState {
                            position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(houseLocation, 15f)
                        }
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = rememberMarkerState(position = houseLocation),
                                title = house.houseName
                            )
                        }

                        // "Book House" Button
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onViewMoreDetailsClick() },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("View More House Details")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Carousel() {
    val carouselImages = listOf(
        R.drawable.nyumbaone,
        R.drawable.nyumbatwo,
        R.drawable.nyumbathree,
        R.drawable.nyumbafour
    )

    val pagerState = rememberPagerState(
        pageCount = { carouselImages.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { pageIndex ->
            Image(
                painter = painterResource(id = carouselImages[pageIndex]),
                contentDescription = "Carousel Image $pageIndex",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        LaunchedEffect(pagerState.currentPage) {
            while (true) {
                delay(3000)
                val nextPage = (pagerState.currentPage + 1) % carouselImages.size
                pagerState.animateScrollToPage(nextPage)
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            IconButton(
                onClick = {
                    if (pagerState.currentPage > 0) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                enabled = pagerState.currentPage > 0
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }

            IconButton(
                onClick = {
                    if (pagerState.currentPage < carouselImages.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                enabled = pagerState.currentPage < carouselImages.size - 1
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }
    }
}

@Composable
fun MarqueeText(text: String, backgroundColor: Color) {
    val transition = rememberInfiniteTransition()
    val xOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -500f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    Box(modifier = Modifier.padding(16.dp)) {
        Text(
            text = text,
            modifier = Modifier
                .graphicsLayer { translationX = xOffset }
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
