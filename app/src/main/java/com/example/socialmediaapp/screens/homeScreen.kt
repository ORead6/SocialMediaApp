package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.socialmediaapp.components.myNavBar
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.signIn.UserData

@Composable
fun homeScreen(
    userData: UserData?
) {
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


        }

    }
}


@Preview
@Composable
fun DefaultPreviewOfhomeScreen() {
    val userData = UserData(userId = "test", username = null, profilePictureUrl = null)
    homeScreen(userData)
}