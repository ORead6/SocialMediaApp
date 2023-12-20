package com.example.socialmediaapp.databaseCalls

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class databaseCalls (
    private val userId: String
)
{
    private val auth = Firebase.auth

    fun getPosts(completion: (List<String>) -> Unit) {
        val dbReference = Firebase.firestore
        val docRef = dbReference.collection("Posts")
        val postIds = mutableListOf<String>()

        docRef.whereEqualTo("createdBy", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id
                    postIds.add(documentId)
                }
                completion(postIds) // Invoke the completion block with the collected postIds
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun getGroups(completion: (List<String>) -> Unit) {
        val dbReference = Firebase.firestore
        val groupIds = mutableListOf<String>()

        dbReference.collection("Groups").get()
            .addOnSuccessListener { groups ->
                for (group in groups) {
                    val groupId = group.id
                    val usersRef = dbReference.collection("Groups").document(groupId).collection("Users")
                    usersRef.whereEqualTo("uuid", userId).get()
                        .addOnSuccessListener { userDocs ->
                            if (!userDocs.isEmpty) {
                                groupIds.add(groupId)
                            }
                            if (group == groups.last()) {
                                completion(groupIds)
                            }
                        }
                        .addOnFailureListener {

                        }
                }
            }
            .addOnFailureListener {

            }
    }

}