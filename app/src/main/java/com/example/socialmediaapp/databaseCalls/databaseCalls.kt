package com.example.socialmediaapp.databaseCalls

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

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
                val tasks = mutableListOf<Task<QuerySnapshot>>()
                val groupList = groups.documents.toList()

                for (group in groupList) {
                    val groupId = group.id
                    val usersRef =
                        dbReference.collection("Groups").document(groupId).collection("Users")
                    val userQuery = usersRef.whereEqualTo("uuid", userId).get()

                    tasks.add(userQuery)
                }

                Tasks.whenAllSuccess<QuerySnapshot>(tasks)
                    .addOnSuccessListener { userDocsList ->
                        userDocsList.forEachIndexed { index, userDocs ->
                            if (!userDocs.isEmpty) {
                                groupIds.add(groupList[index].id)
                            }
                        }
                        completion(groupIds)
                    }
                    .addOnFailureListener {
                        // Handle failure
                    }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun getGroupsInfo(groupIDs: List<String>, completion: (Map<String, Map<String, String>>) -> Unit) {
        val dbReference = Firebase.firestore
        val groupInfoMap = mutableMapOf<String, Map<String, String>>()

        val tasks = mutableListOf<Task<DocumentSnapshot>>()

        groupIDs.forEach { groupId ->
            val groupDocRef = dbReference.collection("Groups").document(groupId)
            val task = groupDocRef.get()
            tasks.add(task)
        }

        Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
            .addOnSuccessListener { snapshots ->
                snapshots.forEachIndexed { index, snapshot ->
                    val groupId = groupIDs[index]
                    val groupData = snapshot.data?.toMap() ?: emptyMap()
                    groupInfoMap[groupId] = groupData as Map<String, String>
                }
                completion(groupInfoMap)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                completion(emptyMap()) // Or handle failure appropriately
            }
    }

    fun getGroupName(groupID: String, completion: (String) -> Unit) {
        val dbRef = Firebase.firestore
        val groupCollection = dbRef.collection("Groups")

        dbRef.collection("Groups").document(groupID).get()
            .addOnSuccessListener { theGroup ->
                val theGroupName = theGroup.get("name")
                completion(theGroupName.toString())
            }

            .addOnFailureListener { exception ->
                completion("FAILED")
            }
    }
}