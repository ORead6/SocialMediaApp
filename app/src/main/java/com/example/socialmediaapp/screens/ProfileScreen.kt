package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.socialmediaapp.components.EditProfileButton
import com.example.socialmediaapp.components.FollowerCounter
import com.example.socialmediaapp.components.FollowingCounter
import com.example.socialmediaapp.components.GridScreen
import com.example.socialmediaapp.components.GroupsCounter
import com.example.socialmediaapp.components.bioSection
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.components.postDivider
import com.example.socialmediaapp.components.userNameDisplay
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navBarController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            myDarkGrey,
                            myGradientGrey
                        )
                    )
                )
        ) {

            val dbCalls = databaseCalls(userData?.userId ?: "")

            var userPosts by remember {
                mutableStateOf(mutableListOf<String>())
            }

            val context = LocalContext.current


            LaunchedEffect(Unit) {
                dbCalls.getPosts {theIds ->
                    userPosts = theIds.toMutableList()
                }
            }

            Spacer(modifier = Modifier.padding(5.dp))
            userNameDisplay(userData?.username)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, top = 14.dp, end = 28.dp)
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
            ) {
                bioSection(thisBio = userData?.bio.toString())
                Spacer(modifier = Modifier.padding(10.dp))
                EditProfileButton(thisOnClick = {
                    navBarController.navigate("EditProfile")
                })
            }

            Spacer(modifier = Modifier.padding(25.dp))

            postDivider()

            GridScreen(userPosts, dbCalls, navBarController)
        }


    }
}

