package com.example.socialmediaapp.components

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.R

@Composable
fun groupNameDisplay(groupName: String) {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (groupName != null) {
            Text(
                textAlign = TextAlign.Start,
                text = groupName,
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            )
        } else {
            Text(
                textAlign = TextAlign.Start,
                text = "GROUP",
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            )
        }
    }
}


@Composable
fun overviewButton(modifier: Modifier = Modifier, thisOnClick: () -> Unit) {

    Column (modifier = modifier
        .padding(start = 8.dp, end = 8.dp)) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Overview",
                    style = TextStyle(
                        fontSize = 11.sp ,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}

@Composable
fun leaderboardButton(modifier: Modifier = Modifier, thisOnClick: () -> Unit) {

    Column (modifier = modifier
        .padding(start = 8.dp, end = 8.dp)) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Leaderboard",
                    style = TextStyle(
                        fontSize = 11.sp ,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}

@Composable
fun mediaButton(modifier: Modifier = Modifier, thisOnClick: () -> Unit) {

    Column (modifier = modifier
        .padding(start = 8.dp, end = 8.dp)) {
        Button(
            onClick = thisOnClick ,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Media",
                    style = TextStyle(
                        fontSize = 11.sp ,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}

@Composable
fun groupPhoto(selectedImageUri: Uri?){

    if (selectedImageUri != null) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RectangleShape)
                .border(width = 1.dp, color = Color.White, shape = RectangleShape),
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.noimage),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(RectangleShape)
                .border(width = 1.dp, color = Color.White, shape = RectangleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun groupGridScreen(
    groupPosts: MutableList<String>,
    navController: NavHostController,
    mediaMap: MutableMap<String, Uri?>,
    typeMap: MutableMap<String, String>,
    thumbNailMap: MutableMap<Any, Bitmap?>,
    loading: Boolean
) {

    if (loading) {
        // Show loading indicator or placeholder

    } else {

        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(groupPosts) { post ->
                val uri = mediaMap[post]
                val type = typeMap[post]
                if (uri != null && type != null) {
                    groupMediaItem(post, uri, type, navController, thumbNailMap[post])
                }
            }
        }
    }
}

@Composable
fun groupMediaItem(
    post: String,
    uri: Uri,
    type: String,
    navController: NavHostController,
    bitmap: Bitmap?
) {
    if (post != "") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(width = (0.25f).dp, color = Color.Black),
            contentAlignment = Alignment.Center
        ) {
            val image = isImage(type)

            if (image) {
                AsyncImage(
                    model = uri,
                    contentDescription = "PostMedia",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clickable {
                        val uriToSend = uri.toString()
                        val encodedUri = Uri.encode(uriToSend)
                        val postType = "img"

                        try {
                            navController.navigate("PostViewer/${encodedUri}/${postType}/${post}")
                        } catch (e: IllegalArgumentException) {
                            Log.d("NAVERROR", e.toString())
                        }
                    }
                )
            } else {
                val thumbnail = remember(uri) {
                    mutableStateOf<Bitmap?>(null)
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "PostMedia",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val uriToSend = uri.toString()
                                val encodedUri = Uri.encode(uriToSend)
                                val postType = "vid"

                                try {
                                    navController.navigate("PostViewer/${encodedUri}/${postType}/${post}")
                                } catch (e: IllegalArgumentException) {
                                    Log.d("NAVERROR", e.toString())
                                }
                            }
                    )
                }

            }

        }
    }
}

