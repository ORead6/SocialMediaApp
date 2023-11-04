package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.socialmediaapp.components.myNavBar

@Composable
fun InboxScreen() {
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
            Text(text = "Inbox")
        }

    }
}
