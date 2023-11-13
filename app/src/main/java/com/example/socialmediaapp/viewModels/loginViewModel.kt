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

class LoginViewModel() : ViewModel(){
    private val auth = Firebase.auth

    private val _email = mutableStateOf("")
    val emailVal: State<String> = _email

    private val _pass = mutableStateOf("")
    val passVal: State<String> = _pass

    fun setEmailVal(text: String) {
        _email.value = text
    }

    fun setPassVal(pass: String) {
        _pass.value = pass
    }

    fun login(email: String, pass: String, navController: NavController) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("home")
                } else {
                    Log.d("LOGINFAIL", task.exception?.message.toString())
                }
            }
    }
}
