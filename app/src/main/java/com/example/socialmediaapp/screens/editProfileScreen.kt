package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socialmediaapp.components.Header
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.myCustomFont
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.signIn.UserData

@Composable
fun EditProfileScreen (
    userData: UserData?,
    onSignOut: () -> Unit = {},
    navController: NavController? = null
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreensColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LoginScreensColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, top = 14.dp, end = 28.dp),
            ) {
                backButton (thisOnClick = {
                    navController?.navigate("")
                })
                Header(value = "Profile")
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfEditProfileScreen() {
    EditProfileScreen(null)
}