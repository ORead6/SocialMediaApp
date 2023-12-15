package com.example.socialmediaapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.socialmediaapp.components.myNavBar
import com.example.socialmediaapp.screens.LoginScreen
import com.example.socialmediaapp.screens.LoginSelectionScreen
import com.example.socialmediaapp.screens.RegisterScreen
import com.example.socialmediaapp.signIn.GoogleAuthUiClient
import com.example.socialmediaapp.signIn.SignInViewModel
import com.example.socialmediaapp.signIn.UserData
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context  =  applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            val navController = rememberNavController()

            val myNavGraph = navController.createGraph(startDestination = "loginSelection") {
                
                composable(
                    route = "loginSelection",
                    content = {
                        val viewModel = viewModel<SignInViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        val userSignedIn = remember { mutableStateOf<UserData?>(null) }

                        LaunchedEffect(key1 = Unit) {
                            googleAuthUiClient.getSignedInUser().thenAccept { userData ->
                                userSignedIn.value = userData
                            }
                        }

                        if (userSignedIn.value != null) {
                            navController.navigate("home")
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult  = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign In Successful",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate("home")
                                viewModel.resetState()
                            }
                        }

                        if (userSignedIn.value == null) {
                            LoginSelectionScreen(
                                state = state,
                                onSignInClick1 = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                navController = navController
                            )
                        }
                    }
                )

                composable(
                    route = "home",
                    content = {

                        val userSignedIn = remember { mutableStateOf<UserData?>(null) }


                        googleAuthUiClient.getSignedInUser().thenAccept { userData ->
                            userSignedIn.value = userData
                        }

                        Log.d("USERDATA", userSignedIn.value?.userPosts.toString())


                        myNavBar(userData = userSignedIn.value,
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed Out",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("loginSelection")
                                }
                            })

                    }
                )

                composable(
                    route = "register",
                    content = {
                        RegisterScreen(navController)
                    }
                )

                composable(
                    route = "login",
                    content = {
                        LoginScreen(navController)
                    }
                )
            }

            NavHost(navController = navController, graph = myNavGraph)
        }

    }
}