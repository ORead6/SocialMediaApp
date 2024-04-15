package com.example.socialmediaapp.viewModels


import android.net.Uri
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

class editprofileViewModel() : ViewModel(){

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _bio = mutableStateOf("")
    val bio: State<String> = _bio

    private val _pfp =  mutableStateOf<Uri?>(null)
    val pfp: State<Uri?> = _pfp

    private val _oldpfp =  mutableStateOf<Uri?>(null)
    val oldpfp: State<Uri?> = _oldpfp

    fun setBio(text: String) {
        _bio.value = text
    }

    fun setUsername(text: String) {
        _username.value = text
    }

    fun setPfp(newUri: Uri?) {
        _pfp.value = newUri
    }

    fun setOldPfp(newUri: Uri?) {
        _oldpfp.value = newUri
    }

}
