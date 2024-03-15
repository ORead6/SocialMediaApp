package com.example.socialmediaapp.viewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class uploadViewModel() : ViewModel() {

    private val auth = Firebase.auth

    private val _caption = mutableStateOf("")
    val caption: State<String> = _caption

    private val _progressVal = mutableStateOf(0f)
    val progressVal: State<Float> = _progressVal

    fun setCaption(text: String) {
        _caption.value = text
    }

    fun setProgress(value: Double) {
        _progressVal.value = value.toFloat()
    }

    fun createPost(
        value: Uri?,
        selectedOption: String,
        myViewModel: uploadViewModel,
        dbCalls: databaseCalls
    ) {
        if (value == null) {
            Log.d("POSTUPLOAD", "Uri is null. Cannot upload Media.")
            return
        }

        val db = Firebase.firestore
        val postsCollection = db.collection("Posts")

        val currentUser = auth.currentUser?.uid

        dbCalls.getGroupIdWithName(selectedOption) {groupID ->
            currentUser?.let {userID ->
                val postData = hashMapOf(
                    "createdBy" to userID,
                    "caption" to _caption.value,
                    "postedTo" to groupID
                )

                postsCollection.add(postData)
                    .addOnSuccessListener {

                        val documentID = it.id
                        val storage = Firebase.storage
                        val path = "Posts/${documentID}/postMedia"

                        val storageRef = storage.reference.child(path)

                        storageRef.putFile(value)
                            .addOnSuccessListener {
                                // Handle successful upload
                                Log.d("POSTUPLOAD", "Media uploaded successfully")
                            }

                            .addOnFailureListener { exception ->
                                // Handle failed upload
                                Log.d("POSTUPLOAD", "Failed to upload Media: ${exception.printStackTrace()}")
                                exception.printStackTrace()
                            }

                            .addOnProgressListener { taskSnapshot ->
                                // You can use this listener to track upload progress if needed
                                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                                myViewModel.setProgress(progress)
                                Log.d("POSTUPLOAD", "Upload is $progress% done")
                            }

                    }


                    .addOnFailureListener {
                        Log.d("POSTUPLOAD", "FAILURE: ${it.toString()}")
                    }
            }
        }



    }

}