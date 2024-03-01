package com.example.socialmediaapp.screens

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.components.FollowerCounter
import com.example.socialmediaapp.components.FollowingCounter
import com.example.socialmediaapp.components.GroupsCounter
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.editGroupPhoto
import com.example.socialmediaapp.components.groupNameDisplay
import com.example.socialmediaapp.components.groupPrivacyButton
import com.example.socialmediaapp.components.header
import com.example.socialmediaapp.components.leaderboardButton
import com.example.socialmediaapp.components.mediaButton
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.myViewModel
import com.example.socialmediaapp.components.newGroupName
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.overviewButton
import com.example.socialmediaapp.components.pfpCircle
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.viewModels.createGroupViewModel
import com.example.socialmediaapp.viewModels.groupPreviewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun CreateGroupScreen () {

    //val createViewModel = createGroupViewModel()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = myGradientGrey)
            .padding(top = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    backButton(thisOnClick = {
                        //navController.navigate("Groups")
                    })
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    header()
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

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
                    ) {
                        editGroupPhoto()
                        newGroupName()
                    }

                    groupPrivacyButton()
                }


            }
        }
    }
}


