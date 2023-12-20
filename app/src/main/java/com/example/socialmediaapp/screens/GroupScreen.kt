package com.example.socialmediaapp.screens

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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.SearchBar
import com.example.socialmediaapp.components.groupGrid
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData

@Composable
fun GroupScreen(
    userData: UserData?
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreensColor)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(LoginScreensColor)
                .padding(start = 28.dp, end = 28.dp, top = 14.dp)
        ) {

            val dbCalls = databaseCalls(userData?.userId ?: "")

            var userGroups by remember {
                mutableStateOf(mutableListOf<String>())
            }


            LaunchedEffect(Unit) {
                dbCalls.getGroups {theGroups ->
                    userGroups = theGroups.toMutableList()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Place here thing  at top left V

                    Spacer(modifier = Modifier.weight(1f))

                    // Place Here thing at top right V
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    // Place here thing in middle
                    SearchBar()
                }
            }

            groupGrid(userGroups)
        }

    }
}
