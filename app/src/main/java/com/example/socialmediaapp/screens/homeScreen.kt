package com.example.socialmediaapp.screens

import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.socialmediaapp.components.myCustomFont
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.languageTranslation.getTranslatedString
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.homeViewModel
import fypLikeButton
import kotlin.math.ceil

@OptIn(UnstableApi::class)
@Composable
fun homeScreen(
    userData: UserData?
) {

    val dbCalls = databaseCalls("")

    var dataReady by remember { mutableStateOf(false) }

    var postIds by remember { mutableStateOf(mutableListOf<String>()) }

    var videoUris by remember { mutableStateOf(mutableMapOf<String, Uri?>()) }
    var videoCapt by remember { mutableStateOf(mutableMapOf<String, String>()) }

    var userWeights by remember { mutableStateOf(mutableMapOf<String, Long>()) }

    var currentPost by remember { mutableStateOf("") }

    var isGestureInProgress by remember { mutableStateOf(false) }

    var currPostIndex by remember { mutableStateOf(0) }

    var numberOfLikes = remember {
        mutableIntStateOf(0)
    }

    var userHasLiked = remember {
        mutableStateOf(false)
    }

    var currentCapt = remember {
        mutableStateOf("")
    }

    var homeModel = homeViewModel()

    var mediaItem by remember { mutableStateOf(MediaItem.fromUri(Uri.EMPTY)) }

    val context = LocalContext.current

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(true) {
        // Get Videos
        dbCalls.getVideos(5) { ids ->
            postIds = ids.toMutableList()

            dbCalls.getAllUris(postIds) { videoUriMap ->
                videoUris = videoUriMap.toMutableMap()

                dbCalls.getVideoCaptions(postIds) { captionMap ->
                    videoCapt = captionMap.toMutableMap()

                    dbCalls.getUserWeights { weightMap ->
                        val theWeights = weightMap.toMutableMap()

                        userWeights = theWeights.mapValues { (_, value) -> (value as? Long) ?: 0L } as MutableMap<String, Long>

                        Log.d("VIDEOID", postIds.toString())

                        currentPost = postIds[currPostIndex]

                        dbCalls.getLikesWithPost(postIds[currPostIndex]) { likeNum ->

                            dbCalls.getLikeStatusOfUser(postIds[currPostIndex]) { likeStatus ->

                                numberOfLikes.intValue = likeNum
                                userHasLiked.value = likeStatus
                                currentCapt.value = videoCapt[postIds[currPostIndex]].toString()

                                dataReady = true

                            }

                        }
                    }
                }

            }

        }
    }

    LaunchedEffect(currentPost) {
        if (videoUris[currentPost] != null) {

            Log.d("VIDEOURI", videoUris[currentPost].toString())

            mediaItem = MediaItem.fromUri(videoUris[currentPost]!!)

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            exoPlayer.playWhenReady = true

        }
    }

    LaunchedEffect(currPostIndex) {
        if (currPostIndex < postIds.size) {
            dbCalls.getLikesWithPost(postIds[currPostIndex]) { likeNum ->
                dbCalls.getLikeStatusOfUser(postIds[currPostIndex]) { likeStatus ->
                    numberOfLikes.intValue = likeNum
                    userHasLiked.value = likeStatus
                    currentCapt.value = videoCapt[postIds[currPostIndex]].toString()

                }

            }
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(myGradientGrey) // Set the background color of the entire surface to black
        .pointerInput(Unit) {
            detectVerticalDragGestures(onDragStart = {}, onDragEnd = {
                isGestureInProgress = false
            }, onVerticalDrag = { change, dragAmount ->
                val delta = dragAmount
                if (!isGestureInProgress) {
                    if (delta > 0) {
                        // User swiped down
                        homeModel.swipe("DOWN", context, currPostIndex, postIds) {
                            currPostIndex = it
                            currentPost = postIds[currPostIndex]

                        }
                    } else {
                        // User swiped up
                        homeModel.swipe("UP", context, currPostIndex, postIds) {
                            currPostIndex = it
                            currentPost = postIds[currPostIndex]

                            // Logic for retrieving additional videos
                            // If we are half way through list
                            val pointToCheck = ceil(postIds.size / 2.0).toInt()
                            if (currPostIndex + 1 >= pointToCheck) {
                                dbCalls.getAdditionalVideo(postIds) { additionalVid ->
                                    postIds.add(additionalVid)

                                    Log.d("VIDEODATA: ", "ID: $additionalVid")

                                    dbCalls.getPostMedia(additionalVid) { theUri, _ ->
                                        videoUris[additionalVid] = theUri

                                        Log.d("VIDEODATA: ", "URI: $theUri")

                                        dbCalls.getPostCaption(additionalVid) { capt ->
                                            videoCapt[additionalVid] = capt

                                            // NOW NEED TO ORDER THE LIST BASED ON USER WEIGHTS
                                            dbCalls.orderOnWeights(
                                                postIds, videoCapt, userWeights, currPostIndex
                                            ) { newVideoList ->
                                                postIds = newVideoList.toMutableList()

                                                dbCalls.getLikesWithPost(postIds[currPostIndex]) { likeNum ->
                                                    numberOfLikes.value = likeNum


                                                    dbCalls.getLikeStatusOfUser(postIds[currPostIndex]) { likeStatus ->
                                                        userHasLiked.value = likeStatus
                                                    }

                                                }
                                            }

                                        }
                                    }

                                }

                            }

                        }
                    }

                    isGestureInProgress = true
                }
            })
        }) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = myGradientGrey), contentAlignment = Alignment.Center
        ) {

            if (!dataReady) {
                CircularProgressIndicator(color = myGradientGrey)
            } else {
                var lifecycle by remember {
                    mutableStateOf(Lifecycle.Event.ON_CREATE)
                }

                if (videoUris[currentPost] != null) {

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

                    AndroidView(modifier = Modifier
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
                        })

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 8.dp), contentAlignment = Alignment.CenterEnd
                    ) {
                        fypLikeButton(numberOfLikes.value, userHasLiked.value) {
                            // What to do when user presses the like button
                            if (userHasLiked.value) {
                                // User has liked and wants to unlike
                                dbCalls.removeLikeFromPost(postIds[currPostIndex]) {
                                    dbCalls.getLikesWithPost(postIds[currPostIndex]) {
                                        userHasLiked.value = false
                                        numberOfLikes.value = it

                                        dbCalls.interactionChange(currentCapt.value, userWeights, "-") {newWeights ->
                                            userWeights = newWeights as MutableMap<String, Long>
                                        }
                                    }
                                }

                            } else {
                                // User has not liked and wants to like
                                dbCalls.addLikeToPost(postIds[currPostIndex]) {
                                    dbCalls.getLikesWithPost(postIds[currPostIndex]) {
                                        userHasLiked.value = true
                                        numberOfLikes.value = it

                                        dbCalls.interactionChange(currentCapt.value, userWeights, "+") {newWeights ->
                                            userWeights = newWeights as MutableMap<String, Long>
                                        }

                                    }
                                }
                            }
                        }
                    }

                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp, bottom = 100.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = currentCapt.value,
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        )
                    }

                }
            }
        }
    }
}
