package com.example.socialmediaapp.screens

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.socialmediaapp.components.Header
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.messaging.messagingDataStruc
import com.example.socialmediaapp.viewModels.directMessageViewModel
import com.google.firebase.Timestamp
import messageCardGrid
import messageField
import sendMsgButton

@OptIn(UnstableApi::class)
@Composable
fun DirectMessagingScreen(
    userMessageID: String = "",
    navController: NavController
) {

    var dataReady by remember { mutableStateOf(false) }
    val dbCalls = databaseCalls("")
    val messageList = remember { mutableStateOf<List<messagingDataStruc>>(emptyList()) }
    val theContext = LocalContext.current

    var myViewModel = directMessageViewModel()

    val context = LocalContext.current
    var keyboardController = LocalSoftwareKeyboardController.current

    var text = remember { mutableStateOf("") }

    var messagedUsername = remember { mutableStateOf("") }

    LaunchedEffect(dataReady) {
        dbCalls.getMessages(userMessageID) {returnedList ->
            messageList.value = returnedList.toList()

            messageList.value = messageList.value.sortedBy { it.timestamp }.toList()

            dbCalls.getUsername(userMessageID) {
                messagedUsername.value = it
                dataReady = true
            }


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
            .padding(start = 4.dp, end = 4.dp, top = 4.dp)
        ) {

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start) {
                backButton(thisOnClick = {
                    navController.navigate("Inbox")
                })
                Box(modifier = Modifier.fillMaxWidth(0.9f),
                    contentAlignment = Alignment.Center)
                {
                    Header(value = messagedUsername.value)
                }


            }


            if (dataReady) {
                Spacer(Modifier.weight(1f))

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    if (messageList.value.size > 0) {
                        messageCardGrid(messageList, userMessageID)
                    }
                }

                Spacer(Modifier.padding(2.dp))

                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    messageField(textvalue = text, myViewModel = myViewModel)

                    Spacer(Modifier.padding(2.dp))

                    sendMsgButton {
                        val msgToSend = myViewModel.msg

                        if (msgToSend.value != "") {
                            keyboardController?.hide()

                            text.value = ""

                            val newMsg  = messagingDataStruc(
                                senderID = "",
                                receiverID = userMessageID,
                                msg = msgToSend.value,
                                timestamp = Timestamp.now()
                            )

                            dbCalls.sendMessage(newMsg)

                            messageList.value = messageList.value.toMutableList().apply {
                                add(newMsg)
                                myViewModel.setMsg("")
                            }

                            messageList.value = messageList.value.sortedBy { it.timestamp }.toList()
                        } else {
                            Toast.makeText(theContext, "Message field cannot be blank", Toast.LENGTH_SHORT).show()
                        }
                    }

                }


                Spacer(modifier = Modifier.padding(40.dp))

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
