package com.example.socialmediaapp.screens

import android.net.Uri
import android.os.LocaleList
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.components.EditProfileButton
import com.example.socialmediaapp.components.FollowerCounter
import com.example.socialmediaapp.components.FollowingCounter
import com.example.socialmediaapp.components.GridScreen
import com.example.socialmediaapp.components.GroupsCounter
import com.example.socialmediaapp.components.bioSection
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.components.postDivider
import com.example.socialmediaapp.components.userNameDisplay
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData
import java.util.Locale

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navBarController: NavController
) {

    val dbCalls = databaseCalls(userData?.userId ?: "")

    val userBio = remember { mutableStateOf("") }
    val thisUsername = remember { mutableStateOf("") }
    val followerCount = remember { mutableIntStateOf(0) }
    val followingCount = remember { mutableIntStateOf(0) }
    var userPosts by remember { mutableStateOf(mutableListOf<String>()) }
    val mediaMap by remember { mutableStateOf(mutableMapOf<String, Uri?>()) }
    val typeMap by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var groupCount by remember { mutableIntStateOf(0) }
    val profilePicUri = remember { mutableStateOf<Uri?>(null) }

    val dataReady = remember { mutableStateOf(false) }



    LaunchedEffect(true) {
        dbCalls.getUserBio {
            userBio.value = it

            dbCalls.getPosts {theIds ->
                userPosts = theIds.toMutableList()

                var count = 0

                dbCalls.getGroups {groupIds ->
                    groupCount = groupIds.size

                    dbCalls.getUsername {username ->
                        thisUsername.value = username

                        dbCalls.getPfp { thePfp ->
                            profilePicUri.value = thePfp

                            userPosts.forEach { post ->
                                dbCalls.getPostMedia(post) { uri, postType ->
                                    mediaMap[post] = uri
                                    typeMap[post] = postType
                                    count++
                                }
                            }

                            if (count == userPosts.size - 1) {
                                Log.d("POSTING", userPosts.toString())


                                dbCalls.getFollowing {following ->
                                    followingCount.value = following.size

                                    dbCalls.getFollowers {followers ->
                                        followerCount.value = followers

                                        dataReady.value = true
                                    }

                                }


                            }
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
                Spacer(modifier = Modifier.padding(5.dp))
                userNameDisplay(thisUsername.value)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, top = 14.dp, end = 28.dp)
                ) {
                    pfpCircle(profilePicUri)
                    GroupsCounter(number = groupCount, modifier = Modifier.weight(1f))
                    FollowerCounter(number = followerCount.value, modifier = Modifier.weight(1f))
                    FollowingCounter(number = followingCount.value, modifier = Modifier.weight(1f))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, top = 10.dp, end = 28.dp)
                ) {
                    bioSection(userBio.value)
                    Spacer(modifier = Modifier.padding(10.dp))
                    EditProfileButton(thisOnClick = {
                        navBarController.navigate("EditProfile")
                    })
                }

                Spacer(modifier = Modifier.padding(25.dp))

                postDivider()

                GridScreen(userPosts, navBarController, mediaMap, typeMap)
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

private fun setLocale(localeToSet: String) {
    val localeListToSet = LocaleList(Locale(localeToSet))
    LocaleList.setDefault(localeListToSet)
}
