@file:OptIn(ExperimentalAnimationApi::class)

package com.example.socialmediaapp.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import java.time.format.TextStyle

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedItem: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myNavBar() {

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNews = false
        ),

        BottomNavigationItem(
            title = "Groups",
            selectedIcon = Icons.Filled.Groups,
            unselectedItem = Icons.Outlined.Groups,
            hasNews = true
        ),

        BottomNavigationItem(
            title = "Upload",
            selectedIcon = Icons.Filled.AddCircleOutline,
            unselectedItem = Icons.Outlined.AddCircleOutline,
            hasNews = false
        ),

        BottomNavigationItem(
            title = "Inbox",
            selectedIcon = Icons.Filled.Message,
            unselectedItem = Icons.Outlined.Message,
            hasNews = true,
            badgeCount = 10
        ),

        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedItem = Icons.Outlined.Person,
            hasNews = false
        )

    )

    var selectedItemIndex  by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(Color.Black)
                    .height(75.dp),
                containerColor = Color.Black,
                contentColor = Color.Black,
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            //  navController.navigate(item.title)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Black
                        ),
                        alwaysShowLabel = true,
                        label = {
                            val isVisible = (index == selectedItemIndex)

                            AnimatedContent(targetState = isVisible, content = {isVisible ->
                                if(isVisible) {
                                    Text(text = item.title, color = Color.White)
                                } else {
                                    Text(text = item.title, color = myDarkGrey)
                                }
                            },
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "")
                        },
                        icon = {

                            val isVisible = (index == selectedItemIndex)

                            BadgedBox(
                                badge = {
                                    if(item.badgeCount != null) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if(item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                
                                AnimatedContent(targetState = isVisible, content = {isVisible ->
                                    if(isVisible) {
                                        Icon(
                                            imageVector = item.selectedIcon,
                                            contentDescription = item.title,
                                            tint = Color.White)
                                    } else {
                                        Icon(
                                            imageVector = item.unselectedItem,
                                            contentDescription = item.title,
                                            tint = Color.White)
                                    }
                                },
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "")

                            }
                        })
                }
            }
        }
    ) {

    }
}