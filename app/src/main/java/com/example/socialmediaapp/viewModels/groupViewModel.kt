package com.example.socialmediaapp.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.socialmediaapp.signIn.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class groupViewModel() : ViewModel(){
    private val auth = Firebase.auth

    private val _groupID = mutableStateOf("")
    val groupID: State<String> = _groupID

    fun setGroupID(ID: String) {
        _groupID.value = ID
    }

}
