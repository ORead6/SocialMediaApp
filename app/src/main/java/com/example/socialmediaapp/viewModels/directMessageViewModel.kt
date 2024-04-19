package com.example.socialmediaapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class directMessageViewModel : ViewModel() {

    private val _msg = mutableStateOf("")
    val msg = _msg


    fun setMsg(newMsg: String) {
        _msg.value = newMsg
    }

}