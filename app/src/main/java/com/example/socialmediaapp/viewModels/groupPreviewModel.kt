package com.example.socialmediaapp.viewModels


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.socialmediaapp.signIn.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class groupPreviewModel() : ViewModel(){

    private val _overview = mutableStateOf(true)
    val overview: State<Boolean> = _overview

    private val _leaderboard = mutableStateOf(true)
    val leaderboard: State<Boolean> = _leaderboard

    private val _media = mutableStateOf(true)
    val media: State<Boolean> = _media

    fun setOverview(state: Boolean) {
        _overview.value = state
    }

    fun setLeaderboard(state: Boolean) {
        _leaderboard.value = state
    }

    fun setMedia(state: Boolean) {
        _media.value = state
    }

}
