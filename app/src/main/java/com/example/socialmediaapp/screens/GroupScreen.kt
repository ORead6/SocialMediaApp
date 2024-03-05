package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.socialmediaapp.components.SearchBar
import com.example.socialmediaapp.components.addGroup
import com.example.socialmediaapp.components.groupGrid
import com.example.socialmediaapp.components.joinButton
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.groupViewModel

@Composable
fun GroupScreen(
    userData: UserData?,
    navBarController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            myDarkGrey,
                            myGradientGrey
                        )
                    )
                )
                .padding(start = 28.dp, end = 28.dp, top = 14.dp)
        ) {

            val dbCalls = databaseCalls(userData?.userId ?: "")

            var userGroups by remember {
                mutableStateOf(mutableListOf<String>())
            }

            var userGroupData by remember {
                mutableStateOf<Map<String, Map<String, String>>>(emptyMap())
            }

            val myViewModel = groupViewModel()

            LaunchedEffect(userGroups) {
                dbCalls.getGroups {theGroups ->
                    userGroups = theGroups.toMutableList()
                }

                dbCalls.getGroupsInfo(userGroups) { theGroupsData ->
                    userGroupData = theGroupsData
                }
            }

            val context = LocalContext.current

            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                SearchBar(myViewModel)
                Spacer(modifier = Modifier.padding(2.dp))
                joinButton(thisOnClick = {
                    dbCalls.joinGroup(myViewModel.searchInv.value, context, navBarController)
                })
            }

            addGroup(thisOnClick = {
                navBarController.navigate("CreateGroup")
            })

            groupGrid(userGroupData, dbCalls) { groupID ->
                navBarController.navigate("GroupPreview/${groupID.toString()}")
            }

        }

    }
}
