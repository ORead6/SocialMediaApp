package com.example.socialmediaapp.screens

import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.socialmediaapp.components.FollowerCounter
import com.example.socialmediaapp.components.FollowingCounter
import com.example.socialmediaapp.components.GridScreen
import com.example.socialmediaapp.components.GroupsCounter
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.bioSection
import com.example.socialmediaapp.components.followUserButton
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.components.postDivider
import com.example.socialmediaapp.components.userNameDisplay
import com.example.socialmediaapp.databaseCalls.databaseCalls

@Composable
fun viewOtherProfileScreen(
    userID: String,
    navBarController: NavController,
    groupID: String
) {

    val dbCalls = databaseCalls("")

    val userBio = remember { mutableStateOf("") }

    val thisUsername = remember { mutableStateOf("") }

    val followingStatus = remember { mutableStateOf(false) }

    var userPosts by remember {
        mutableStateOf(mutableListOf<String>())
    }

    val mediaMap by remember { mutableStateOf(mutableMapOf<String, Uri?>()) }
    val typeMap by remember { mutableStateOf(mutableMapOf<String, String>()) }

    var followerCount = remember { mutableIntStateOf(0) }
    var followingCount = remember { mutableIntStateOf(0) }

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

            dbCalls.getPosts(userID) { theIds ->
                userPosts = theIds.toMutableList()

                var count = 0

                dbCalls.getGroups(userID) { groupIds ->
                    groupCount = groupIds.size

                    dbCalls.getUsername(userID) { username ->
                        thisUsername.value = username

                        dbCalls.getPfp(userID) { thePFP ->
                            pfp.value = thePFP

                            dbCalls.getFollowingStatus(userID) { followStatus ->
                                followingStatus.value = followStatus

                                userPosts.forEach { post ->
                                    dbCalls.getPostMedia(post) { uri, postType ->
                                        mediaMap[post] = uri
                                        typeMap[post] = postType
                                        count++
                                    }
                                }

                                if (count == userPosts.size - 1 || userPosts.size == 0 && count == 0) {
                                    Log.d("POSTING", userPosts.toString())

                                    dbCalls.getFollowers(userID) { followers ->
                                        followerCount.value = followers

                                        dbCalls.getFollowingSize(userID) { followingSize ->
                                            followingCount.value = followingSize

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
                Row(
                    modifier = Modifier
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
                    pfpCircle(pfp)
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

                    followUserButton(followingStatus.value) {
                        followingStatus.value = !followingStatus.value

                        if (followingStatus.value) {
                            dbCalls.addFollowing(userThatIsViewed = userID) {

                            }
                        } else {
                            dbCalls.removeFollowing(userThatIsViewed = userID) {

                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(25.dp))

            postDivider()

            GridScreen(userPosts, navBarController, mediaMap, typeMap)
        }
    }
}


