package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData

@Composable
fun homeScreen(
    userData: UserData?
) {

    val dbCalls = databaseCalls("")

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
            Text(text = "Home")

            Button(onClick = {
                dbCalls.createMyUsers()
            }) {
                Text(text = "Add Users")
            }
        }

    }
}


@Preview
@Composable
fun DefaultPreviewOfhomeScreen() {
    val userData = UserData(userId = "test", username = null, profilePictureUrl = null, bio = null)
    homeScreen(userData)
}