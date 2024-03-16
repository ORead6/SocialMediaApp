package com.example.socialmediaapp.components

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun pfpCircle(
    userData: UserData?
) {
    if (userData?.profilePictureUrl != null && userData.profilePictureUrl != "") {
        AsyncImage(
            model = userData?.profilePictureUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.userpfp),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun GroupsCounter(
    number: Int = 12,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Groups",
            fontFamily = myCustomFont,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = number.toString(),
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun FollowerCounter(
    number: Int = 473,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Followers",
            fontFamily = myCustomFont,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = number.toString(),
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun FollowingCounter(
    number: Int = 274,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Following",
            fontFamily = myCustomFont,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = number.toString(),
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun bioSection(
    thisBio: String? = null,
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        if (thisBio != null) {
            Text(
                textAlign = TextAlign.Start,
                text = thisBio,
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun EditProfileButton(
    thisOnClick: () -> Unit = {},
)  {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Edit profile",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = myCustomFont)
            }
        }
    }
}

@Composable
fun postDivider() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
          imageVector = Icons.Filled.GridOn,
            contentDescription = "myPosts",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.padding(5.dp))

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            thickness = 1.dp
        )

    }

}

@Composable
fun userNameDisplay(username: String? = "Test") {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (username != null) {
            Text(
                textAlign = TextAlign.Start,
                text = username,
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            Text(
                textAlign = TextAlign.Start,
                text = "username",
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun GridScreen(
    userPosts: MutableList<String>,
    dbCalls: databaseCalls,
    navBarController: NavController
) {
    val mediaMap by remember { mutableStateOf(mutableMapOf<String, Uri?>()) }
    val typeMap by remember { mutableStateOf(mutableMapOf<String, String>()) }

    LaunchedEffect(userPosts) {
        userPosts.forEach { post ->
            dbCalls.getPostMedia(post) { uri, postType ->
                mediaMap[post] = uri
                typeMap[post] = postType
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(userPosts) { post ->
            val uri = mediaMap[post]
            val type = typeMap[post]
            if (uri != null) {
                if (type != null) {
                    GridItem(post, uri, type, navBarController)
                }
            }
        }
    }
}

@Composable
fun GridItem(item: String, uri: Uri, type: String, navBarController: NavController) {
    if (item != "") {
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

                        try {
                            navBarController.navigate("PostViewer/${encodedUri}")
                        } catch (e: IllegalArgumentException) {
                            Log.d("NAVERROR", e.toString())
                        }
                    }
                )
            } else {
                val thumbnail = remember(uri) {
                    mutableStateOf<Bitmap?>(null)
                }

                LaunchedEffect(uri) {
                    val loadedThumbnail = loadVideoThumbnail(uri)
                    thumbnail.value = loadedThumbnail
                }

                thumbnail.value?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "PostMedia",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val uriToSend = uri.toString()
                                navBarController.navigate("PostViewer")
                            }
                    )
                }
            }

        }
    }
}

fun isImage(type: String): Boolean {
    val lowerCaseType = type.lowercase()

    // Check if the type contains the word "image"
    return lowerCaseType.contains("image")
}

private suspend fun loadVideoThumbnail(uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(uri.toString())
            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }
}


