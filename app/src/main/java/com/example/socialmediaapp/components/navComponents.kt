package com.example.socialmediaapp.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.socialmediaapp.screens.LoginScreen
import com.example.socialmediaapp.screens.homeScreen
import kotlin.math.sign

@Composable
fun appNavigation() {

    val navController = rememberNavController()

    val myNavGraph = navController.createGraph(startDestination = "login") {
        composable(
            route = "login",
            content = {
                LoginScreen(signInButton = {navController.navigate("home")})
            }
        )

        composable(
            route = "home",
            content = {
                homeScreen()
            }
        )
    }

    NavHost(navController = navController, graph = myNavGraph)



}
