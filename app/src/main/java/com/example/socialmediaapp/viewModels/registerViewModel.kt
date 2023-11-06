package com.example.socialmediaapp.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class registerViewModel : ViewModel(){
    private val _email = mutableStateOf("")
    val emailVal: State<String> = _email

    private val _username = mutableStateOf("")
    val usernameVal: State<String> = _username

    private val _pass1 = mutableStateOf("")
    val pass1Val: State<String> = _pass1

    private val _pass2 = mutableStateOf("")
    val pass2Val: State<String> = _pass2

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
}