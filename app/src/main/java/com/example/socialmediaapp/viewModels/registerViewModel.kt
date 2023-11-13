package com.example.socialmediaapp.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.socialmediaapp.signIn.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class registerViewModel() : ViewModel(){
    private val auth = Firebase.auth

    val createUserResult: MutableLiveData<FirebaseAuthResult> = MutableLiveData()

    private val _email = mutableStateOf("")
    val emailVal: State<String> = _email

    private val _username = mutableStateOf("")
    val usernameVal: State<String> = _username

    private val _pass1 = mutableStateOf("")
    val pass1Val: State<String> = _pass1

    private val _pass2 = mutableStateOf("")
    val pass2Val: State<String> = _pass2

    private val _currentUser = mutableStateOf(UserData("", null, null))
    val currentUser: MutableState<UserData> = _currentUser

    fun setEmailVal(text: String) {
        _email.value = text
    }

    fun setPass1Val(text: String) {
        _pass1.value = text
    }

    fun setPass2Val(text: String) {
        _pass2.value = text
    }

    fun setUsernameVal(text: String) {
        _username.value = text
    }

    fun setUserData(thisUser: UserData) {
        _currentUser.value = thisUser
    }

    fun registerUser(
        email: String,
        username: String,
        password: String,
        navController: NavHostController
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createUserResult.value = FirebaseAuthResult.Success(auth.currentUser)

                    val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    auth.currentUser?.updateProfile(userProfileChangeRequest)

                    Log.d("owen-read", "Successful")

                    navController.navigate("home")

                } else {
                    createUserResult.value = FirebaseAuthResult.Error(task.exception)
                    task.exception?.message?.let { Log.d("owen", it) }
                }
            }
    }
}

sealed class FirebaseAuthResult {
    data class Success(val user: FirebaseUser?) : FirebaseAuthResult()
    data class Error(val exception: Exception?) : FirebaseAuthResult()
}