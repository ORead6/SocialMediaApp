package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.components.EditProfileButton
import com.example.socialmediaapp.components.FollowerCounter
import com.example.socialmediaapp.components.FollowingCounter
import com.example.socialmediaapp.components.GroupsCounter
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.bioSection
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.components.postDivider
import com.example.socialmediaapp.components.userNameDisplay
import com.example.socialmediaapp.signIn.UserData

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
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
            Spacer(modifier = Modifier.padding(5.dp))
            userNameDisplay(userData?.username)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, top = 14.dp, end = 28.dp)
                    .background(LoginScreensColor)
            ) {
                pfpCircle(userData = userData)
                GroupsCounter(modifier = Modifier.weight(1f))
                FollowerCounter(modifier = Modifier.weight(1f))
                FollowingCounter(modifier = Modifier.weight(1f))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, top = 10.dp, end = 28.dp)
                    .background(LoginScreensColor)
            ) {
                bioSection(thisBio = "This is a testing bio, I want to see if the text will wrap if it keeps going over the end of the screen")
                Spacer(modifier = Modifier.padding(10.dp))
                EditProfileButton()
            }

            Spacer(modifier = Modifier.padding(25.dp))

            postDivider()
        }


    }
}

@Preview
@Composable
fun DefaultPreviewOfProfileScreen() {
    val userData = UserData(userId = "test", username = null, profilePictureUrl = null)
    ProfileScreen(userData, onSignOut = {})
}
