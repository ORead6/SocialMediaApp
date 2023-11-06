package com.example.socialmediaapp.components

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.socialmediaapp.screens.LoginSelectionScreen
import com.example.socialmediaapp.signIn.GoogleAuthUiClient
import com.example.socialmediaapp.signIn.SignInViewModel

@Composable
fun appNavigation(googleAuthUiClient: GoogleAuthUiClient, applicationContext: Context) {


}

