package com.example.socialmediaapp.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.groupGridScreen
import com.example.socialmediaapp.components.groupNameDisplay
import com.example.socialmediaapp.components.groupPhoto
import com.example.socialmediaapp.components.leaderboardButton
import com.example.socialmediaapp.components.loadVideoThumbnail
import com.example.socialmediaapp.components.mediaButton
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.overviewButton
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.R
import com.example.socialmediaapp.components.AnimatedNumberDisplay
import com.example.socialmediaapp.components.CustomPopupMenu
import com.example.socialmediaapp.components.GroupThreeDotsMenu
import com.example.socialmediaapp.components.myCustomFont
import com.example.socialmediaapp.viewModels.groupPreviewModel
import kotlinx.coroutines.launch

@SuppressLint("ServiceCast")
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

            val leaderBoards = listOf("Weight Lifted", "Weight Loss", "Weight Gain")
            var isExpanded by remember { mutableStateOf(false) }
            var selectedOption by remember { mutableStateOf("Weight Lifted") }

            val groupPrevViewModel = groupPreviewModel()
            groupPrevViewModel.setGroupID(groupID)
            val theContext = LocalContext.current

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

            var totalWeightLifted by remember {
                mutableIntStateOf(0)
            }

            var totalWeightGained by remember {
                mutableIntStateOf(0)
            }

            var totalWeightLost by remember {
                mutableIntStateOf(0)
            }

            var isCurrUserOwner by remember {
                mutableStateOf(false)
            }


            var addWeightExpanded by remember { mutableStateOf(false) }
            var weightToBeAdded by remember { mutableStateOf("") }

            LaunchedEffect(totalWeightLifted) {
                dbCalls.getTotalWeightLifted(groupID) {
                    totalWeightLifted = it
                }
            }

            LaunchedEffect(isCurrUserOwner) {
                dbCalls.getOwnerStatus(groupID) {
                    isCurrUserOwner = it
                }
            }

            LaunchedEffect(totalWeightGained) {
                dbCalls.getTotalWeightGained(groupID) {
                    totalWeightGained = it
                }
            }

            LaunchedEffect(totalWeightLost) {
                dbCalls.getTotalWeightLost(groupID) {
                    totalWeightLost = it
                }
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

                    // Spacer to push GroupThreeDotsMenu to the right
                    Spacer(modifier = Modifier.weight(1f))

                    GroupThreeDotsMenu(isCurrUserOwner, onMenuItemClick = {
                        if (it == "Invite") {
                            dbCalls.getGroupInvite(groupID) { inviteCode ->
                                val clipboard =
                                    theContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData = ClipData.newPlainText("Invite Code", inviteCode)

                                clipboard.setPrimaryClip(clipData)

                                Toast.makeText(theContext, "Invite Code Copied!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (it == "Leave") {
                            dbCalls.leaveGroup(groupID) {
                                Toast.makeText(theContext, "Group Successfuly Left", Toast.LENGTH_SHORT).show()
                                navController.navigate("Groups")
                            }
                        }

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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                                .clickable { isExpanded = !isExpanded },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = selectedOption,
                                modifier = Modifier.padding(16.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = myCustomFont,
                                    fontSize = 16.sp
                                )
                            )

                            DropdownMenu(
                                modifier = Modifier
                                    .background(color = Color.White)
                                    .padding(16.dp),
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                            ) {

                                leaderBoards.forEach { groupName ->
                                    DropdownMenuItem(
                                        onClick = {
                                            isExpanded = false
                                            selectedOption = groupName
                                        },
                                        text = { Text(text = groupName) }
                                    )
                                }
                            }
                        }

                        when (selectedOption) {
                            "Weight Lifted" -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(Alignment.TopEnd)
                                            .padding(end = 16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Total Weight Lifted",
                                                style = TextStyle(
                                                    fontSize = 25.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = myCustomFont
                                                )
                                            )

                                            Spacer(modifier = Modifier.weight(1f))

                                            Image(
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .clickable {
                                                        addWeightExpanded = !addWeightExpanded
                                                    },
                                                painter = painterResource(id = R.drawable.plus),
                                                contentDescription = "Plus",
                                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(color = myGradientGrey)
                                            )
                                        }

                                        AnimatedNumberDisplay(totalWeightLifted)

                                    }

                                    if (addWeightExpanded) {
                                        CustomPopupMenu(
                                            onDismiss = {
                                                addWeightExpanded = false
                                                weightToBeAdded = ""
                                                },
                                            textField1Value = weightToBeAdded,
                                            onTextField1ValueChanged = { weightToBeAdded = it },
                                            addWeightFunc = {
                                                groupPrevViewModel.addWeightToDBLifted(weightToBeAdded, theContext) {
                                                    dbCalls.getTotalWeightLifted(groupID) {
                                                        totalWeightLifted = it
                                                    }
                                                }
                                                addWeightExpanded = false
                                                weightToBeAdded = ""

                                            }
                                        )
                                    }
                                }
                            }
                            "Weight Gain" -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(Alignment.TopEnd)
                                            .padding(end = 16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Total Weight Gained",
                                                style = TextStyle(
                                                    fontSize = 25.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = myCustomFont
                                                )
                                            )

                                            Spacer(modifier = Modifier.weight(1f))

                                            Image(
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .clickable {
                                                        addWeightExpanded = !addWeightExpanded
                                                    },
                                                painter = painterResource(id = R.drawable.plus),
                                                contentDescription = "Plus",
                                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(color = myGradientGrey)
                                            )
                                        }

                                        AnimatedNumberDisplay(totalWeightGained)

                                    }

                                    if (addWeightExpanded) {
                                        CustomPopupMenu(
                                            onDismiss = {
                                                addWeightExpanded = false
                                                weightToBeAdded = ""
                                            },
                                            textField1Value = weightToBeAdded,
                                            onTextField1ValueChanged = { weightToBeAdded = it },
                                            addWeightFunc = {
                                                groupPrevViewModel.addWeightToDBGained(weightToBeAdded, theContext) {
                                                    dbCalls.getTotalWeightGained(groupID) {
                                                        totalWeightGained = it
                                                    }
                                                }
                                                addWeightExpanded = false
                                                weightToBeAdded = ""

                                            }
                                        )
                                    }
                                }
                                
                            }
                            "Weight Loss" -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(Alignment.TopEnd)
                                            .padding(end = 16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Total Weight Lost",
                                                style = TextStyle(
                                                    fontSize = 25.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = myCustomFont
                                                )
                                            )

                                            Spacer(modifier = Modifier.weight(1f))

                                            Image(
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .clickable {
                                                        addWeightExpanded = !addWeightExpanded
                                                    },
                                                painter = painterResource(id = R.drawable.plus),
                                                contentDescription = "Plus",
                                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(color = myGradientGrey)
                                            )
                                        }

                                        AnimatedNumberDisplay(totalWeightLost)

                                    }

                                    if (addWeightExpanded) {
                                        CustomPopupMenu(
                                            onDismiss = {
                                                addWeightExpanded = false
                                                weightToBeAdded = ""
                                            },
                                            textField1Value = weightToBeAdded,
                                            onTextField1ValueChanged = { weightToBeAdded = it },
                                            addWeightFunc = {
                                                groupPrevViewModel.addWeightToDBLost(weightToBeAdded, theContext) {
                                                    dbCalls.getTotalWeightLost(groupID) {
                                                        totalWeightLost = it
                                                    }
                                                }
                                                addWeightExpanded = false
                                                weightToBeAdded = ""

                                            }
                                        )
                                    }
                                }
                                
                            }
                        }
                    }

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