package com.example.socialmediaapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.components.FollowerCounter
import com.example.socialmediaapp.components.FollowingCounter
import com.example.socialmediaapp.components.GroupsCounter
import com.example.socialmediaapp.components.groupNameDisplay
import com.example.socialmediaapp.components.leaderboardButton
import com.example.socialmediaapp.components.mediaButton
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.myViewModel
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.overviewButton
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.viewModels.groupPreviewModel

@Composable
fun GroupPreviewScreen (
    groupID: String
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

            LaunchedEffect(groupName) {
                dbCalls.getGroupName(groupID) {theGroupName ->
                    groupName = theGroupName
                }
            }

            Spacer(modifier = Modifier.padding(10.dp))

            groupNameDisplay(groupName)

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
                    Text(text = "Overview")
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
                    Text(text = "Media")
                }
            }





        }
    }
}

@Composable
@Preview
fun previewOfScreen() {
    GroupPreviewScreen("Test")
}