package com.example.socialmediaapp.viewModels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class groupOptionsViewModel : ViewModel() {
    private val _groupName = mutableStateOf("")
    val groupName: State<String> = _groupName

    private val _groupDesc = mutableStateOf("")
    val groupDesc: State<String> = _groupDesc

    private val _groupPriv = mutableStateOf(false)
    val groupPriv: State<Boolean> = _groupPriv

    private val _groupPhoto =  mutableStateOf<Uri?>(null)
    val groupPhoto: State<Uri?> = _groupPhoto

    private val _oldGroupPhoto =  mutableStateOf<Uri?>(null)
    val oldGroupPhoto: State<Uri?> = _oldGroupPhoto

    fun setName(text: String) {
        _groupName.value = text
    }

    fun setDesc(text: String) {
        _groupDesc.value = text
    }

    fun setPriv(value: Boolean) {
        _groupPriv.value = value
    }

    fun setPhoto(newUri: Uri?) {
        _groupPhoto.value = newUri
    }

    fun setOldPhoto(newUri: Uri?) {
        _oldGroupPhoto.value = newUri
    }

}