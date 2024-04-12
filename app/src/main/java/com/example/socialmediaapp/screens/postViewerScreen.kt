package com.example.socialmediaapp.screens

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.socialmediaapp.components.likeButton
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.postBackButton
import com.example.socialmediaapp.components.postCaption
import com.example.socialmediaapp.databaseCalls.databaseCalls

@OptIn(UnstableApi::class) @Composable
fun postViewerScreen(
    uri: String,
    navController: NavHostController,
    postType: String,
    postID: String,
    groupID: String? = ""
) {

    var numberOfLikes = remember {
        mutableIntStateOf(0)
    }

    var userHasLiked  = remember {
        mutableStateOf(false)
    }

    var thePostCaption = remember {
        mutableStateOf("")
    }

    val thisUri = Uri.parse(uri)
    val context = LocalContext.current

    val dbCalls = databaseCalls("")

    // Name at Top Left
    var userPost = remember {
        mutableStateOf("")
    }

    LaunchedEffect(true) {
        dbCalls.getUserWithPost(postID) {
            userPost.value = it
        }
    }

    LaunchedEffect(userHasLiked) {
        dbCalls.getLikesWithPost(postID) {likeNum ->
            dbCalls.getLikeStatusOfUser(postID) {likeStatus ->
                userHasLiked.value = likeStatus
                numberOfLikes.value = likeNum
            }
        }
    }

    LaunchedEffect(userHasLiked) {

    }

    LaunchedEffect(thePostCaption) {
        dbCalls.getPostCaption(postID) {
            thePostCaption.value = it
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradientGrey)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(myGradientGrey)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp)
            ) {

                val backStackEntry = navController.previousBackStackEntry

                postBackButton {

                    Log.d("BACKSTACKENTRY", backStackEntry?.destination?.route.toString())

                    if (backStackEntry?.destination?.route == "Profile"){
                        navController.popBackStack()
                    }

                    if (backStackEntry?.destination?.route == "GroupPreview/{groupID}/{page}") {
                        navController.navigate("GroupPreview/${groupID}/media")
                    }


                }

                Text(text = userPost.value, color = Color.White,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                // Content of the middle column
                if (postType == "img") {
                    AsyncImage(
                        model = thisUri,
                        contentDescription = "Media",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    var lifecycle by remember {
                        mutableStateOf(Lifecycle.Event.ON_CREATE)
                    }

                    val mediaItem = MediaItem.fromUri(thisUri)

                    val exoPlayer = remember {
                        ExoPlayer.Builder(context).build().apply {
                            setMediaItem(mediaItem)
                            prepare()
                            repeatMode = ExoPlayer.REPEAT_MODE_ALL
                            playWhenReady = true
                        }
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            lifecycle = event
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            exoPlayer.release()
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    var isPlaying by remember { mutableStateOf(true) }

                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(myGradientGrey)
                            .clickable {
                                isPlaying = !isPlaying
                                if (isPlaying) {
                                    exoPlayer.play()
                                } else {
                                    exoPlayer.pause()
                                }
                            },
                        factory = {
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = false // Hide controls
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp)
            ) {
                // Content of the bottom column

                likeButton(numberOfLikes.value, userHasLiked.value) {
                    // What to do when user presses the like button
                    if (userHasLiked.value) {
                        // User has liked and wants to unlike
                        dbCalls.removeLikeFromPost(postID) {
                            dbCalls.getLikesWithPost(postID) {
                                userHasLiked.value = false
                                numberOfLikes.value = it
                            }
                        }

                    } else {
                        // User has not liked and wants to like
                        dbCalls.addLikeToPost(postID) {
                            dbCalls.getLikesWithPost(postID) {
                                userHasLiked.value = true
                                numberOfLikes.value = it
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                postCaption(thePostCaption.value)
            }



        }

    }
}