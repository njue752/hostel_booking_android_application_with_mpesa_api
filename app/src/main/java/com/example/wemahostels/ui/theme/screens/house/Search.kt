package com.example.wemahostels.ui.theme.screens.house

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wemahostels.data.HouseViewModel
import com.example.wemahostels.models.House

@Composable
fun SearchScreen(houseViewModel: HouseViewModel, onSearchResultClick: (House) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Houses") },
            trailingIcon = {
                IconButton(onClick = {
                    if (searchQuery.isNotEmpty()) {
                        houseViewModel.searchHouses(searchQuery, context)
                    } else {
                        Toast.makeText(context, "Enter a search term", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Display search results in cards
        val searchResults by houseViewModel.searchResults
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchResults) { house ->
                // Card for each house result
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onSearchResultClick(house) },
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    // Content inside the card
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "House Name: ${house.houseName}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Description: ${house.description}")
                        // Add more details if necessary
                    }
                }
            }
        }
    }
}
