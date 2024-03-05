package com.example.socialmediaapp.viewModels

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class uploadViewModel() : ViewModel() {

    private val _caption = mutableStateOf("")
    val caption: State<String> = _caption

    fun setCaption(text: String) {
        _caption.value = text
    }

}