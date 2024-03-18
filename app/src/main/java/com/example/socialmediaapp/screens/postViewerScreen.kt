package com.example.socialmediaapp.screens

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Lifecycling
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.components.myGradientGrey

@OptIn(UnstableApi::class) @Composable
fun postViewerScreen(uri: String, navController: NavHostController, postType: String) {
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
            val thisUri = Uri.parse(uri)
            val context = LocalContext.current

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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp) // Set one-third of the screen height
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth()
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

        }

    }
}