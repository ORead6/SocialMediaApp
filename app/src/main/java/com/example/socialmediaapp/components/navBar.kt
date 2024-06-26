@file:OptIn(ExperimentalAnimationApi::class)

package com.example.socialmediaapp.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.socialmediaapp.screens.CreateGroupScreen
import com.example.socialmediaapp.screens.DirectMessagingScreen
import com.example.socialmediaapp.screens.EditProfileScreen
import com.example.socialmediaapp.screens.GroupPreviewScreen
import com.example.socialmediaapp.screens.GroupScreen
import com.example.socialmediaapp.screens.InboxScreen
import com.example.socialmediaapp.screens.ProfileScreen
import com.example.socialmediaapp.screens.groupOptionsScreen
import com.example.socialmediaapp.screens.homeScreen
import com.example.socialmediaapp.screens.postViewerScreen
import com.example.socialmediaapp.screens.uploadMediaScreen
import com.example.socialmediaapp.screens.viewOtherProfileScreen
import com.example.socialmediaapp.signIn.UserData

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
fun myNavBar(
    userData: UserData?,
    onSignOut: () -> Unit,
) {

    val items = listOf(
        // Home Icon
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNews = false
        ),

        // Groups Icon
        BottomNavigationItem(
            title = "Groups",
            selectedIcon = Icons.Filled.Groups,
            unselectedItem = Icons.Outlined.Groups,
            hasNews = false
        ),

        // Upload Icon
        BottomNavigationItem(
            title = "Upload",
            selectedIcon = Icons.Filled.AddCircleOutline,
            unselectedItem = Icons.Outlined.AddCircleOutline,
            hasNews = false
        ),

        // Inbox Icon
        BottomNavigationItem(
            title = "Inbox",
            selectedIcon = Icons.Filled.Message,
            unselectedItem = Icons.Outlined.Message,
            hasNews = true,
            badgeCount = 10
        ),

        // Profile Icon
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedItem = Icons.Outlined.Person,
            hasNews = false
        )

    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    var bottomViewable by rememberSaveable {
        mutableStateOf(true)
    }

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        "Home" -> {
            bottomViewable = true
        }

        "Groups" -> {
            bottomViewable = true
        }

        "Upload" -> {
            bottomViewable = true
        }

        "Inbox" -> {
            bottomViewable = true
        }

        "Profile" -> {
            bottomViewable = true
        }

        "EditProfile" -> {
            bottomViewable = false
        }
    }

    val myNavGraph = navController.createGraph(startDestination = "Home") {

        composable(
            route = "Home",
            content = {
                homeScreen(userData)
            }
        )

        composable(
            route = "Groups",
            content = {
                GroupScreen(userData, navBarController = navController)
            }
        )

        composable(
            route = "Upload",
            content = {
                uploadMediaScreen(userData, navController = navController)
            }
        )

        composable(
            route = "Inbox",
            content = {
                InboxScreen(userData, navBarController = navController)
            }
        )

        composable(
            route = "Profile",
            content = {
                ProfileScreen(userData, onSignOut = onSignOut, navBarController = navController)
            }
        )

        composable(
            route = "EditProfile",
            content = {

                EditProfileScreen(
                    userData = userData,
                    navController = navController,
                    onSignOut = onSignOut
                )
            }
        )

        composable(
            route = "GroupPreview/{groupID}/{page}",
            content = {
                val arguments = navBackStackEntry?.arguments
                val parameter = arguments?.getString("groupID")
                val parameter2 = arguments?.getString("page")
                if (parameter != null) {
                    GroupPreviewScreen(parameter, navController =  navController, page = parameter2)
                }
            }
        )

        composable(
            route = "GroupEdit/{groupID}",
            content = {
                val arguments = navBackStackEntry?.arguments
                val parameter = arguments?.getString("groupID")
                if (parameter != null) {
                    groupOptionsScreen(groupID = parameter, navController = navController)
                }
            }
        )

        composable(
            route = "DirectMessaging/{thatUserID}",
            content = {
                val arguments = navBackStackEntry?.arguments
                val theUserID = arguments?.getString("thatUserID")
                if (theUserID != null) {
                    DirectMessagingScreen(navController = navController, userMessageID = theUserID)
                }

            }
        )

        composable(
            route = "PostViewer/{uri}/{postType}/{postID}/{groupID}",
            content = {
                val arguments = navBackStackEntry?.arguments
                val parameter1 = arguments?.getString("uri")
                val parameter2 = arguments?.getString("postType")
                val parameter3 = arguments?.getString("postID")
                val parameter4 = arguments?.getString("groupID")



                if (parameter1 != null && parameter2 != null && parameter3 != null) {
                    if (parameter4 == "abc") {
                        postViewerScreen(parameter1, navController =  navController, parameter2, parameter3)
                    }  else {
                        postViewerScreen(parameter1, navController =  navController, parameter2, parameter3, groupID = parameter4)
                    }
                }
            }
        )

        composable(
            route = "CreateGroup",
            content = {
                CreateGroupScreen(navController =  navController)
            }
        )

        composable(
            route = "viewOtherProfile/{userID}/{groupID}",
            content = {
                val arguments = navBackStackEntry?.arguments
                val parameter1 = arguments?.getString("userID")
                val parameter2 = arguments?.getString("groupID") ?: ""

                if (parameter1 == null) {
                    viewOtherProfileScreen(userID = "", navBarController = navController, groupID = parameter2)
                }
                else {
                    viewOtherProfileScreen(userID = parameter1, navBarController = navController, groupID = parameter2)
                }



            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        bottomBar = {
            if (bottomViewable) {
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
                                navController.navigate(item.title) {
                                    launchSingleTop = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Black
                            ),
                            alwaysShowLabel = true,
                            label = {
                                val isVisible = (index == selectedItemIndex)

                                AnimatedContent(
                                    targetState = isVisible, content = { isVisible ->
                                        if (isVisible) {
                                            Text(text = item.title, color = Color.White)
                                        } else {
                                            Text(text = item.title, color = myDarkGrey)
                                        }
                                    },
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = ""
                                )
                            },
                            icon = {

                                val isVisible = (index == selectedItemIndex)

                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if (item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {

                                    AnimatedContent(
                                        targetState = isVisible, content = { isVisible ->
                                            if (isVisible) {
                                                Icon(
                                                    imageVector = item.selectedIcon,
                                                    contentDescription = item.title,
                                                    tint = Color.White
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = item.unselectedItem,
                                                    contentDescription = item.title,
                                                    tint = Color.White
                                                )
                                            }
                                        },
                                        label = ""
                                    )

                                }
                            })
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, graph = myNavGraph)
    }
}