package com.example.socialmediaapp.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.messaging.messagingDataStruc
import messageCardGrid

@OptIn(UnstableApi::class)
@Composable
fun DirectMessagingScreen(
    userMessageID: String = "",
    navController: NavController
) {

    var dataReady by remember { mutableStateOf(false) }
    val dbCalls = databaseCalls("")
    val messageList = remember { mutableStateOf<List<messagingDataStruc>>(emptyList()) }



    LaunchedEffect(dataReady) {
        dbCalls.getMessages(userMessageID) {
            messageList.value = it.toList()

            dataReady = true
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .background(myGradientGrey)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(myGradientGrey)
        ) {
            if (dataReady) {

                messageCardGrid(messageList, userMessageID)

            } else {

                Box (
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = offWhiteBack)
                }


            }
        }

    }
}
