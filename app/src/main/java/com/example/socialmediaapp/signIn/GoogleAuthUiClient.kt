package com.example.socialmediaapp.signIn

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.socialmediaapp.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
){
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run  {
                    UserData(
                        userId  = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        bio = ""
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
    fun getSignedInUser(): CompletableFuture<UserData?> {

        // WILL RETURN USER DATA BASED ON THE DATA BASE

        val completableFuture = CompletableFuture<UserData?>()
        val uid = auth.currentUser?.uid ?: ""

        if (uid == "") {
            return completableFuture
        }

        val dbReference = Firebase.firestore
        val docRef = dbReference.collection("Users").document(uid)

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val thisUser: UserData

            if (documentSnapshot != null && documentSnapshot.exists()) {

                // RETRIEVES GOOGLE USER DATA FROM THE DATABASE

                val username = documentSnapshot.getString("username") ?: ""
                val photoLink = documentSnapshot.getString("photoLink") ?: ""
                val bio = documentSnapshot.getString("bio") ?: ""

                thisUser = UserData(
                    userId = uid,
                    username = username,
                    profilePictureUrl = photoLink,
                    bio = bio
                )
                completableFuture.complete(thisUser)

//                dbReference.collection("Users")
//                    .document(uid)
//                    .collection("posts")
//                    .get()
//                    .addOnSuccessListener { querySnapshot ->
//                        val postIds = mutableListOf<String>()
//
//                        for (document in querySnapshot.documents) {
//                            postIds.add(document.id)
//                        }
//
//                        // Update UserData with postIds
//                        thisUser.userPosts = postIds
//
//                    }
//
//                    .addOnFailureListener {
//                        Log.d("DBERROR", it.toString())
//                    }

//                dbReference.collection("Users")
//                    .document(uid)
//                    .collection("groups")
//                    .get()
//                    .addOnSuccessListener { querySnapshot ->
//                        val groupIds = mutableListOf<String>()
//
//                        for (document in querySnapshot.documents) {
//                            groupIds.add(document.id)
//                        }
//
//                        // Update UserData with postIds
//                        thisUser.userGroups = groupIds
//                        completableFuture.complete(thisUser)
//                    }
//
//                    .addOnFailureListener {
//                        completableFuture.complete(thisUser)
//                    }


            } else {

                // CREATES FIRST TIME GOOGLE USER IN DB

                val googleUsername = auth.currentUser?.displayName ?: ""
                val googlePhoto = auth.currentUser?.photoUrl.toString()

                val thisUserMap = mapOf<String, String?>(
                    "username" to googleUsername,
                    "photoLink" to googlePhoto,
                    "bio" to ""
                )

                dbReference.collection("Users")
                    .document(uid)
                    .set(thisUserMap)


                completableFuture.complete(null)
            }
        }

        return completableFuture
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}