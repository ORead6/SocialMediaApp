package com.example.socialmediaapp.screens

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData

@OptIn(UnstableApi::class) @Composable
fun homeScreen(
    userData: UserData?
) {

    val dbCalls = databaseCalls("")

    var dataReady by remember { mutableStateOf(false) }

    var postIds by remember { mutableStateOf(mutableListOf<String>()) }

    var videoUris by remember { mutableStateOf(mutableMapOf<String, Uri?>()) }
    var videoCapt by remember { mutableStateOf(mutableMapOf<String, String>()) }

    var userWeights by remember  { mutableStateOf(mutableMapOf<String, Any>()) }

    var currentPost by remember { mutableStateOf("") }

    var isGestureInProgress by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        // Get Videos
        dbCalls.getVideos {ids ->
            postIds = ids.toMutableList()

            dbCalls.getAllUris(postIds) { videoUriMap ->
                videoUris = videoUriMap.toMutableMap()

                dbCalls.getVideoCaptions(postIds) {captionMap ->
                    videoCapt = captionMap.toMutableMap()

                    dbCalls.getUserWeights {weightMap ->
                        userWeights = weightMap.toMutableMap()

                        currentPost = postIds[0]

                        dataReady = true
                    }
                }

            }

        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Set the background color of the entire surface to black
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {},
                    onDragEnd = {
                        isGestureInProgress = false
                    },
                    onVerticalDrag = { change, dragAmount ->
                        val delta = dragAmount
                        if (!isGestureInProgress) {
                            if (delta > 0) {
                                // User swiped down
                                Log.d("SCROLLINGACTIVITY", "DOWN")
                            } else {
                                // User swiped up
                                Log.d("SCROLLINGACTIVITY", "UP")
                            }

                            isGestureInProgress = true
                        }
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current

            if (!dataReady) {
                CircularProgressIndicator(color = myGradientGrey,)
            } else {
                var lifecycle by remember {
                    mutableStateOf(Lifecycle.Event.ON_CREATE)
                }

                if (videoUris[currentPost] != null) {
                    val mediaItem = MediaItem.fromUri(videoUris[currentPost]!!)

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

                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(myGradientGrey),
                        factory = {
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = false // Hide controls
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        }
                    )
                }
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