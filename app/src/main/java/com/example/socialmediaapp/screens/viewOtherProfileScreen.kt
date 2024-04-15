package com.example.socialmediaapp.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.bioSection
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.components.postDivider
import com.example.socialmediaapp.components.userNameDisplay
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData

@Composable
fun viewOtherProfileScreen(
    userID: String,
    navBarController: NavController,
    groupID: String
) {

    val dbCalls = databaseCalls("")

    val userBio = remember { mutableStateOf("") }

    val thisUsername = remember { mutableStateOf("") }

    var userPosts by remember {
        mutableStateOf(mutableListOf<String>())
    }

    var pfp = remember {
        mutableStateOf<Uri?>(null)
    }

    var groupCount by remember {
        mutableIntStateOf(0)
    }

    var dataReady = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        dbCalls.getUserBio(userID) {
            userBio.value = it

            dbCalls.getPosts(userID) {theIds ->
                userPosts = theIds.toMutableList()

                dbCalls.getGroups(userID) {groupIds ->
                    groupCount = groupIds.size

                    dbCalls.getUsername(userID) {username ->
                        thisUsername.value = username

                        dbCalls.getPfp(userID) { thePFP ->
                            pfp.value = thePFP
                            dataReady.value = true
                        }


                    }
                }
            }
        }
    }

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

            if (dataReady.value) {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, top = 5.dp, end = 28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    backButton {
                        navBarController.navigate("GroupPreview/${groupID}/leaderboard")
                    }
                    userNameDisplay(thisUsername.value)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, top = 14.dp, end = 28.dp)
                ) {
                    pfpCircle(pfp)    // GET USER PROFILE PIC
                    GroupsCounter(number = groupCount, modifier = Modifier.weight(1f))
                    FollowerCounter(modifier = Modifier.weight(1f))
                    FollowingCounter(modifier = Modifier.weight(1f))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, top = 10.dp, end = 28.dp)
                ) {
                    bioSection(userBio.value)
                    Spacer(modifier = Modifier.padding(10.dp))

                    // ADD FOLLOW BUTTON
                }

                Spacer(modifier = Modifier.padding(25.dp))

                postDivider()

                GridScreen(userPosts, dbCalls, navBarController)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = offWhiteBack)
                }
            }
        }


    }
}

