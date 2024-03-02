package com.example.socialmediaapp.databaseCalls

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

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
                val theGroupName = theGroup.get("groupName")
                completion(theGroupName.toString())
            }

            .addOnFailureListener { exception ->
                completion("FAILED")
            }
    }

    fun getGroupPhoto(id: String, completion: (Uri?) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val fileRef = storage.reference.child("Groups/${id}/groupPhoto.jpg")

        fileRef.downloadUrl
            .addOnSuccessListener {
                completion(it)
            }

            .addOnFailureListener {e ->
                Log.d("Image Retreival Error", e.toString())
                completion(null)
            }
    }

    fun joinGroup(invCode: String, context: Context, navBarController: NavController) {
        val inviteCode = invCode.lowercase()
        val db = Firebase.firestore
        val groupsCollection = db.collection("Groups")
        val currentUserUUID = auth.currentUser?.uid

        if (currentUserUUID != null) {
            groupsCollection
                .whereEqualTo("inviteCode", inviteCode)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val groupID = document.id
                        val usersCollection = groupsCollection.document(groupID).collection("Users")

                        // Check if the user already exists in the "Users" collection
                        usersCollection
                            .whereEqualTo("uuid", currentUserUUID)
                            .get()
                            .addOnSuccessListener { userDocuments ->
                                if (userDocuments.isEmpty) {
                                    // User does not exist, add the user
                                    val userData = hashMapOf(
                                        "uuid" to currentUserUUID
                                        // Add other fields as needed
                                    )

                                    usersCollection
                                        .add(userData)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Joined Group", Toast.LENGTH_SHORT).show()
                                            navBarController.navigate("Groups")
                                        }
                                        .addOnFailureListener { e ->
                                            // Handle failure
                                        }
                                } else {
                                    // User already exists in the group
                                    Toast.makeText(context, "Already a member of this group", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle any errors during the user query
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Group Code does not Exist", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle the case where currentUserUUID is null
        }
    }
}