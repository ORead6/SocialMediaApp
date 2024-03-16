package com.example.socialmediaapp.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun postViewerScreen(uri: String, navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val thisUri = Uri.parse(uri)
            AsyncImage(
                model = thisUri,
                contentDescription = "Media",
                modifier = Modifier.fillMaxSize()
            )
        }

    }
}