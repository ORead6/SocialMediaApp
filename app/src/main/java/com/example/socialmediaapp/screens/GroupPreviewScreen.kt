package com.example.socialmediaapp.screens

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.groupGridScreen
import com.example.socialmediaapp.components.groupMediaItem
import com.example.socialmediaapp.components.groupNameDisplay
import com.example.socialmediaapp.components.groupPhoto
import com.example.socialmediaapp.components.isImage
import com.example.socialmediaapp.components.leaderboardButton
import com.example.socialmediaapp.components.loadVideoThumbnail
import com.example.socialmediaapp.components.mediaButton
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.overviewButton
import com.example.socialmediaapp.databaseCalls.databaseCalls
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun GroupPreviewScreen (
    groupID: String,
    navController: NavHostController
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = myGradientGrey)
        ) {

            var state by remember {
                mutableStateOf("overview")
            }


            val dbCalls = databaseCalls("")

            var groupName by remember {
                mutableStateOf("")
            }

            var groupPhoto by remember {
                mutableStateOf<Uri?>(null)
            }

            var groupPosts by remember {
                mutableStateOf(mutableListOf<String>())
            }

            var mediaMap by remember {
                mutableStateOf(mutableMapOf<String, Uri?>())
            }

            var typeMap by remember {
                mutableStateOf(mutableMapOf<String, String>())
            }

            var loading by remember {
                mutableStateOf(true)
            }

            var thumbNailMap by remember {
                mutableStateOf(mutableMapOf<Any, Bitmap?>())
            }


            LaunchedEffect(groupPhoto) {
                dbCalls.getGroupPhoto(groupID) {  thePhoto ->
                    groupPhoto = thePhoto
                }
            }

            LaunchedEffect(groupName) {
                dbCalls.getGroupName(groupID) {theGroupName ->
                    groupName = theGroupName
                }
            }

            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(groupPosts) {
                dbCalls.getGroupPosts(groupID) { theIds ->
                    groupPosts = theIds.toMutableList()
                    groupPosts.forEach { post ->
                        dbCalls.getPostMedia(post) { uri, postType ->
                            mediaMap[post] = uri
                            typeMap[post] = postType

                            // Check if all media has been retrieved
                            if (mediaMap.size == groupPosts.size && typeMap.size == groupPosts.size) {
                                loading = false
                            }

                            coroutineScope.launch {
                                val uri = mediaMap[post]
                                if (uri != null) {
                                    val thumbnail = loadVideoThumbnail(uri)
                                    thumbNailMap[post] = thumbnail
                                    Log.d("Gets Here", thumbnail.toString())
                                } else {
                                    Log.d("Gets Here", "Uri is null")
                                }
                            }

                        }
                    }
                }
            }


            Spacer(modifier = Modifier.padding(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    backButton(thisOnClick = {
                        navController.navigate("Groups")
                    })
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    groupNameDisplay(groupName)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 14.dp, end = 4.dp)
            ) {
                overviewButton(modifier = Modifier.weight(1f), thisOnClick = {
                    state = "overview"
                })
                leaderboardButton(modifier = Modifier.weight(1f), thisOnClick = {
                    state = "leaderboard"
                })
                mediaButton(modifier = Modifier.weight(1f), thisOnClick = {
                    state = "media"
                })
            }

            Spacer(modifier = Modifier.padding(10.dp))

            if (state == "overview") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = offWhiteBack,
                            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize()
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, top = 10.dp, end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            groupPhoto(groupPhoto)

                        }
                    }
                }
            }

            if (state == "leaderboard") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = offWhiteBack,
                            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                ) {
                    Text(text = "Leaderboard")
                }
            }

            if (state == "media") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = offWhiteBack,
                            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                ) {
                    groupGridScreen(groupPosts, navController, mediaMap, typeMap, thumbNailMap, loading)
                }
            }





        }
    }
}

//@Composable
//@Preview
//fun previewOfScreen() {
//    GroupPreviewScreen("Test", navController)
//}