package com.example.socialmediaapp.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databaseCalls.databaseCalls
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

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
    loading: Boolean,
    groupID: String
) {

    Log.d("GROUP MEDIA", groupPosts.toString())

    var isLoading by remember { mutableStateOf(loading) }

    if (loading) {

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = myGradientGrey)
                LaunchedEffect(Unit) {
                    delay(2000) // Display for 2 seconds (adjust the time as needed)
                    // Set loading to false after the delay
                    isLoading = false
                }
            } else {
                Text(text = "No Media on the Group...",
                    style = TextStyle(
                        fontFamily = myCustomFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

    } else {

        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(groupPosts) { post ->
                val uri = mediaMap[post]
                val type = typeMap[post]
                if (uri != null && type != null) {
                    groupMediaItem(post, uri, type, navController, thumbNailMap[post], groupID)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun groupMediaItem(
    post: String,
    uri: Uri,
    type: String,
    navController: NavHostController,
    bitmap: Bitmap?,
    groupID: String
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
            val coroutine = rememberCoroutineScope()

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
                            navController.navigate("PostViewer/${encodedUri}/${postType}/${post}/${groupID}")
                        } catch (e: IllegalArgumentException) {
                            Log.d("NAVERROR", e.toString())
                        }
                    }
                )
            } else {

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
                                    navController.navigate("PostViewer/${encodedUri}/${postType}/${post}/${groupID}")
                                } catch (e: IllegalArgumentException) {
                                    Log.d("NAVERROR", e.toString())
                                }
                            }
                    )
                } else {
                    // Retry getting that thumbnail
                    var theBitmap by remember { mutableStateOf<Bitmap?>(null) }
                    coroutine.launch {
                         theBitmap = loadVideoThumbnail(uri)
                    }

                    if (theBitmap != null) {
                        Image(
                            bitmap = theBitmap!!.asImageBitmap(),
                            contentDescription = "PostMedia",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val uriToSend = uri.toString()
                                    val encodedUri = Uri.encode(uriToSend)
                                    val postType = "vid"

                                    try {
                                        navController.navigate("PostViewer/${encodedUri}/${postType}/${post}/${groupID}")
                                    } catch (e: IllegalArgumentException) {
                                        Log.d("NAVERROR", e.toString())
                                    }
                                }
                        )
                    } else {
                        CircularProgressIndicator(color = myGradientGrey)
                    }



                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPopupMenu(
    onDismiss: () -> Unit,
    textField1Value: String,
    onTextField1ValueChanged: (String) -> Unit,
    addWeightFunc: () -> Unit,
) {
    Column(
        Modifier
            .padding(16.dp)
            .background(color = myGradientGrey)
            .padding(16.dp)
    ) {
        Text(
            text = "Enter Weight Lifted:",
            style = TextStyle(
                color = Color.White,
                fontFamily = myCustomFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )

        TextField(
            value = textField1Value,
            onValueChange = { newValue ->
                val filteredValue = newValue.replace("\n", "")
                onTextField1ValueChanged(filteredValue)
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontFamily = myCustomFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = addWeightFunc,
                modifier = Modifier
                    .weight(1f) // Set equal weight for both buttons
                    .height(40.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(buttonGrey),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text(text = "Add", color = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp)) // Add space between buttons

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f) // Set equal weight for both buttons
                    .height(40.dp)
                    .padding(horizontal = 8.dp), // Adjusted padding
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(buttonGrey),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Red)
            ) {
                Text(text = "Close", color = Color.White)
            }
        }
    }
}

@Composable
fun AnimatedNumberDisplay(numberVal: Int) {
    var animatedValue by remember { mutableStateOf(0) }

    val animationSpec = tween<Int>(durationMillis = 500, easing = LinearEasing)
    val targetValue = numberVal

    LaunchedEffect(numberVal) {
        val magnitude = max(1, (Math.log10(targetValue.toDouble()) + 1).toInt())
        val steps = magnitude * 10 // Adjust this multiplier as needed
        val stepDuration = animationSpec.durationMillis / steps
        val stepSize = targetValue.toDouble() / steps

        for (i in 0..steps) {
            animatedValue = (i * stepSize).toInt()
            delay(stepDuration.toLong())
        }
        animatedValue = numberVal
    }

    Text(
        text = "${NumberFormat.getNumberInstance(Locale.US).format(animatedValue)} kg",
        style = TextStyle(
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = myCustomFont
        )
    )
}

@Composable
fun GroupThreeDotsMenu(ownerStatus: Boolean, onMenuItemClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .fillMaxWidth(0.1f)
            .fillMaxHeight(0.05f),
    ){
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Image(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "More Options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            DropdownMenuItem(onClick = {
                onMenuItemClick("Invite")
                expanded = false
            }, text = { Text(text = "Copy Invite Code",
                fontFamily = myCustomFont,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            ) })

            // Add some logic here to show this only if the group was created by the user logged in
            if (ownerStatus) {
                DropdownMenuItem(onClick = {
                    onMenuItemClick("GroupOptions")
                    expanded = false
                }, text = {
                    Text(
                        text = "Group Options",
                        fontFamily = myCustomFont,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                })
            }
            // This is to be able to leave the group. If the owner they cant leave they can only delete
            else {
                DropdownMenuItem(onClick = {
                    onMenuItemClick("Leave")
                    expanded = false
                }, text = {
                    Text(
                        text = "Leave Group",
                        fontFamily = myCustomFont,
                        style = TextStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    )
                })
            }
        }
    }
}

@Composable
fun userGrid(
    userIDs: List<String>,
    userNames: Map<String, String>,
    metric: String,
    groupID: String,
    navController: NavHostController
) {
    val spacing = 25.dp

    val dbCalls = databaseCalls("")

    var dataReady by remember {
        mutableStateOf(false)
    }

    var userValues by remember {
        mutableStateOf(mutableMapOf<String, String>())
    }

    dbCalls.getMetricValuesWithUserIds(metric, groupID, userIDs) {
        userValues = it as MutableMap<String, String>
        dataReady = true
    }

    if (dataReady) {

        val list = userValues.toList()

        val sortedList = list.sortedBy { it.second.toDoubleOrNull() }

        val sortedMap = sortedList.toMap().toMutableMap()

        val entries = sortedMap.entries.toList()

            // Iterate through values
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(entries.reversed()) { index, entry ->
                    val userID = entry.key
                    val username = userNames[userID]
                    val metricVal = userValues[userID]

                    if (username != null && metricVal != null) {
                        userGridItem(username = username, metricValue = metricVal, placement = (index + 1).toString(), navController = navController, id = userID, groupID = groupID)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(75.dp))
            CircularProgressIndicator(color = myGradientGrey)

        }
    }
}

@Composable
fun userGridItem(
    username: String,
    metricValue: String,
    placement: String,
    navController: NavHostController,
    id: String,
    groupID: String
){

    var thisUser by remember { mutableStateOf("") }

    LaunchedEffect(thisUser) {
        databaseCalls("").getCurrUser {
            if (it != null) {
                thisUser = it.uid
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        var circleColor = Color(0xffeb9834)

        if (placement == "1") {
            circleColor = Color(0xffFFD700)
        }
        if (placement == "2") {
            circleColor = Color(0xffC0C0C0)
        }
        if (placement == "3") {
            circleColor = Color(0xffCD7F32)
        }

        Card (modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable {
                if (id != thisUser) {
                    navController.navigate("viewOtherProfile/${id}/${groupID}")
                }
            }
            .border(width = 1.dp, color = myGradientGrey, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                //containerColor = textFieldBG
                containerColor = Color.White
            ),
        )
        {
            Row (modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp, start = 32.dp, end = 32.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .drawBehind {
                            drawCircle(
                                color = circleColor,
                                radius = this.size.maxDimension
                            )
                        },
                    text = placement,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = myCustomFont,
                        fontSize = 18.sp,
                        color = Color.White)
                )

                Spacer(Modifier.padding(20.dp))

                Text(
                    text = username,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = myCustomFont,
                        fontSize = 15.sp,
                        color = Color.Black))

                Spacer(Modifier.weight(1f))

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.US).format(metricValue.toInt())} kg",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontFamily = myCustomFont,
                        fontSize = 15.sp,
                        color = Color.Black)
                )
            }
        }
    }
}




