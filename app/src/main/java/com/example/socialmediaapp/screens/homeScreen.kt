package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myNavBar


@Composable
fun homeScreen() {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            //Spacer(modifier = Modifier.weight(1f))
            myNavBar()
        }

    }
}


@Preview
@Composable
fun DefaultPreviewOfhomeScreen() {
    homeScreen()
}