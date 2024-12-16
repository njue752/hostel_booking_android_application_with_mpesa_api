package com.example.wemahostels.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import com.example.wemahostels.R // Make sure the drawable resources are imported

@Composable
fun SocialMediaIcons() {
    val context = LocalContext.current

    // Twitter Icon
    IconButton(onClick = {
        val twitterIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/Martyn_Njue"))
        context.startActivity(twitterIntent)
    }) {
        Box(modifier = Modifier.size(24.dp)) { // Set the size of the Box
            Image(
                painter = painterResource(id = R.drawable.ictwitter), // Your Twitter icon resource
                contentDescription = "Twitter",
                modifier = Modifier.fillMaxSize(), // Make the image take up the entire Box
                contentScale = ContentScale.Fit // Ensures the image scales properly to fit
            )
        }
    }

    // LinkedIn Icon
    IconButton(onClick = {
        val linkedInIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/martin-njue-8a1102215/"))
        context.startActivity(linkedInIntent)
    }) {
        Box(modifier = Modifier.size(24.dp)) { // Set the size of the Box
            Image(
                painter = painterResource(id = R.drawable.linkedin), // Your LinkedIn icon resource
                contentDescription = "LinkedIn",
                modifier = Modifier.fillMaxSize(), // Make the image take up the entire Box
                contentScale = ContentScale.Fit // Ensures the image scales properly to fit
            )
        }
    }
}
