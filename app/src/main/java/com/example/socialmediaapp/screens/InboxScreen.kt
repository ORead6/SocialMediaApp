package com.example.socialmediaapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.messaging.messagingDataStruc
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.inboxViewModel
import messageGrid
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    userData: UserData?,
    navBarController: NavController
) {
    val dbCalls = databaseCalls("")
    val viewModel = inboxViewModel(emptyList())
    var dataReady = mutableStateOf(false)
    var userMap = mutableMapOf<String, String>()

    val messageList = remember { mutableStateOf<List<messagingDataStruc>>(emptyList()) }

    LaunchedEffect(dataReady) {
        // Get Users Following

        dbCalls.getFollowing { theUsers ->

            userMap = theUsers.toMutableMap()

            val listOfUsers = mutableListOf<String>()

            for ((key, value) in theUsers) {
                listOfUsers.add(key)
            }

            viewModel.setUserList(listOfUsers)

            dbCalls.getFirstMessages {

                messageList.value = it

                dataReady.value = true
            }



        }
    }



    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradientGrey)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(myGradientGrey)
        ) {

            if (dataReady.value) {

                //Collecting states from ViewModel
                val searchText by viewModel.searchText.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()
                val usersList by viewModel.userList.collectAsState()

                val theContext = LocalContext.current

                SearchBar(
                    query = searchText,
                    onQueryChange = viewModel::onSearchTextChange,
                    onSearch = {
                        if (searchText in userMap.keys) {
                            navBarController.navigate("DirectMessaging/${userMap[searchText]}")
                        } else {
                            Toast.makeText(theContext, "User does not exist", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    active = isSearching,
                    onActiveChange = { viewModel.onToogleSearch() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LazyColumn {
                        itemsIndexed(usersList) { _, username ->
                            Text(
                                text = username,
                                modifier = Modifier
                                    .padding(
                                        start = 8.dp,
                                        top = 4.dp,
                                        end = 8.dp,
                                        bottom = 4.dp
                                    )
                                    .clickable {
                                        viewModel.onSearchTextChange(username)
                                        navBarController.navigate("DirectMessaging/${userMap[username]}")
                                    }
                            )
                        }
                    }
                }

                messageGrid(messageList, thisOnClick = { receiver, sender ->
                    dbCalls.getCurrUser {
                        val userID = it?.uid ?: ""

                        if (receiver != userID) {
                            navBarController.navigate("DirectMessaging/$receiver")
                        }

                        if (sender != userID) {
                            navBarController.navigate("DirectMessaging/$sender")
                        }
                    }

                })

            } else {
                CircularProgressIndicator(color = offWhiteBack)
            }


        }

    }
}
