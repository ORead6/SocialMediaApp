package com.example.socialmediaapp.viewModels


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.math.BigInteger
import java.security.SecureRandom

class createGroupViewModel() : ViewModel(){

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _groupName = mutableStateOf("")
    val groupName: State<String> = _groupName

    private val _groupBio = mutableStateOf("")
    val groupBio: State<String> = _groupBio

    private val _privacyState = mutableStateOf(false)
    val privacyState: State<Boolean> = _privacyState

    fun setGroupName(text: String) {
        _groupName.value = text
    }

    fun setPrivacyState(state: Boolean) {
        _privacyState.value = state
    }

    // 36^6 = 2.176.782.336 possibilities -> For scope there wont be 2 of the same codes
    private fun generateRandomCode(length: Int): String {
        val secureRandom = SecureRandom()
        val randomBytes = ByteArray(length / 2)
        secureRandom.nextBytes(randomBytes)
        return (BigInteger(1, randomBytes).toString(16).padStart(length, '0')).lowercase()
    }

    fun createNewGroup(completion: (String) -> Unit) {

        val currUser = auth.currentUser?.uid ?: ""

        val thisGroup = mapOf<String, String?>(
            "groupName" to groupName.value,
            "groupPhoto" to "",
            "groupBio" to groupBio.value,
            "privacyStatus" to privacyState.value.toString(),
            "inviteCode" to generateRandomCode(6),
            "owner" to currUser
        )

        firestore.collection("Groups")
            .add(thisGroup)
            .addOnSuccessListener { documentReference ->

                val groupUUID = documentReference.id

                val userCollection = firestore.collection("Groups").document(groupUUID).collection("Users")

                val currentUserUUID = auth.currentUser?.uid
                currentUserUUID?.let {
                    val userData = hashMapOf(
                        "uuid" to it
                    )

                    userCollection.add(userData)
                        .addOnSuccessListener {
                            completion(groupUUID)
                        }

                        .addOnFailureListener {e ->
                            Log.d("FIREBASEGROUPS", e.toString())
                        }
                }


                if (currentUserUUID != null) {

                    val thisUser = mapOf<String, String?>(
                        "guuid" to groupUUID,
                    )

                    firestore.collection("Users").document(currentUserUUID).collection("Groups")
                        .add(thisUser)
                }

            }

            .addOnFailureListener { e ->
                Log.d("FIREBASEGROUPS", "DIDNT WORK")
            }

        }

    fun uploadImage(groupUUID: String, value: Uri?) {
        if (value == null) {
            Log.d("IMGUPLOAD", "Uri is null. Cannot upload image.")
            return
        }

        val storage = Firebase.storage

        // Specify the path to the file in Firebase Storage
        val path = "Groups/${groupUUID}/groupPhoto.jpg"

        Log.d("IMGUPLOAD", path)

        // Create a Storage reference to the specified path
        val storageRef = storage.reference.child(path)

        // Upload the file to Firebase Storage
        storageRef.putFile(value)
            .addOnSuccessListener {
                // Handle successful upload
                Log.d("IMGUPLOAD", "Image uploaded successfully")
            }
            .addOnFailureListener { exception ->
                // Handle failed upload
                Log.d("IMGUPLOAD", "Failed to upload image: ${exception.printStackTrace()}")
                exception.printStackTrace()
            }
            .addOnProgressListener { taskSnapshot ->
                // You can use this listener to track upload progress if needed
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                Log.d("IMGUPLOAD", "Upload is $progress% done")
            }
    }

    fun setBio(it: String) {
        _groupBio.value = it
    }
}


