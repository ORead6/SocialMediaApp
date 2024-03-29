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

    fun getGroupPosts(groupID: String, completion: (List<String>) -> Unit) {
        val dbReference = Firebase.firestore
        val docRef = dbReference.collection("Posts")
        val postIds = mutableListOf<String>()

        docRef.whereEqualTo("postedTo", groupID)
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

    fun getGroupNames(completion: (List<String?>) -> Unit) {
        val currentUserUUID = auth.currentUser?.uid
        val db = Firebase.firestore

        val groupsCollectionForUser = db.collection("Users").document(currentUserUUID!!).collection("Groups")
        val groupsCollection = db.collection("Groups")

        groupsCollectionForUser.get()
            .addOnSuccessListener {
                val uuidList = mutableListOf<String?>()

                for (document in it) {
                    val uuid = document.getString("guuid")
                    uuidList.add(uuid)
                }

                Log.d("getGroupNames", uuidList.toString())

                val groupNamesList = mutableListOf<String?>()
                val groupsCollection = db.collection("Groups")

                var completedCount = 0

                for (uuid in uuidList) {
                    if (uuid != null) {
                        groupsCollection.document(uuid).get()
                            .addOnSuccessListener { groupDocument ->
                                val groupName = groupDocument.getString("groupName")
                                groupNamesList.add(groupName)
                                Log.d("getGroupNames", "Group name added: $groupName")

                                // Increment the counter
                                completedCount++

                                // Check if all documents have been processed
                                if (completedCount == uuidList.size) {
                                    // This means the for loop is over
                                    Log.d("getGroupNames", "For loop is over. All documents processed.")
                                    completion(groupNamesList)
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle the failure, e.g., log an error or provide a default value
                                groupNamesList.add(null)
                                Log.e("getGroupNames", "Error getting groupName: $exception")

                                // Increment the counter
                                completedCount++

                                // Check if all documents have been processed
                                if (completedCount == uuidList.size) {
                                    // This means the for loop is over
                                    Log.d("getGroupNames", "For loop is over. All documents processed.")
                                    completion(groupNamesList)
                                }
                            }
                    }
                }
            }

            .addOnFailureListener { exception ->
                // Handle the failure, e.g., log an error or provide a default value
                completion(emptyList())
                Log.e("getGroupNames", "Error getting UUIDs: $exception")
            }

    }

    fun getGroupIdWithName(groupNameSelection: String, completion: (String) -> Unit) {
        val currentUserUUID = auth.currentUser?.uid

        val db = Firebase.firestore
        val groupCollection = db.collection("Groups")

        val groupsCollectionForUser = db.collection("Users").document(currentUserUUID!!).collection("Groups")

        if (groupNameSelection == "Public"){
            completion("Public")
        }

        groupsCollectionForUser.get()
            .addOnSuccessListener {
                val uuidList = mutableListOf<String?>()

                for (document in it) {
                    val uuid = document.getString("guuid")
                    uuidList.add(uuid)
                }

                for (guuid in uuidList) {
                    if (guuid != null) {
                        groupCollection.document(guuid).get()
                            .addOnSuccessListener {group ->
                                val groupName = group.getString("groupName")

                                if (groupName == groupNameSelection) {
                                    completion(guuid)
                                }
                            }
                    }
                }

            }

        }

    fun getPostMedia(post: String, completion: (Uri, String) -> Unit) {

        val storage = FirebaseStorage.getInstance()
        val fileRef = storage.reference.child("/Posts/${post}/postMedia")

        fileRef.downloadUrl
            .addOnSuccessListener {theUri ->

                fileRef.metadata
                    .addOnSuccessListener {
                        Log.d("FILEMETADATA", it.contentType.toString())
                        completion(theUri, it.contentType.toString())
                    }

            }

            .addOnFailureListener {e ->
                Log.d("Image Retreival Error", e.printStackTrace().toString())
            }

    }

    fun getUserWithPost(postID: String, completion: (String) -> Unit) {
        val db = Firebase.firestore
        val postCollection = db.collection("Posts")
        val userCollection = db.collection("Users")

        postCollection.document(postID)
            .get()
            .addOnSuccessListener { postDoc ->
                val createdBy = postDoc.getString("createdBy")

                if (createdBy != null) {
                    userCollection.document(createdBy)
                        .get()
                        .addOnSuccessListener { userDoc ->
                            val userName = userDoc.getString("username")

                            if (userName != null) {
                                completion(userName)
                            }
                        }
                }

            }

    }

    fun getLikesWithPost(postID: String, completion: (Int) -> Unit) {
        val db = Firebase.firestore
        val postCollection = db.collection("Posts")
        val userCollection = db.collection("Users")

        postCollection.document(postID).collection("Likes")
            .get()
            .addOnSuccessListener {
                completion(it.size())
            }

            .addOnFailureListener {
                completion(0)
            }
    }

    fun getLikeStatusOfUser(postID: String, completion: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val postCollection = db.collection("Posts")
        val userCollection = db.collection("Users")

        val currUser = auth.currentUser?.uid

        if (currUser != null) {
            postCollection.document(postID).collection("Likes").document(currUser)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        completion(true)
                    } else {
                        completion(false)
                    }
                }

                .addOnFailureListener {
                    completion(false)
                }
        }
    }

    fun addLikeToPost(postID: String, completion: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val postCollection = db.collection("Posts")

        val currUser = auth.currentUser?.uid

        val likeData = hashMapOf(
            "likes" to "yes"
        )

        if (currUser != null) {
            postCollection.document(postID).collection("Likes").document(currUser)
                .set(likeData)
                .addOnSuccessListener {
                    completion(true)
                }
        }

    }

    fun removeLikeFromPost(postID: String, completion: () -> Unit) {
        val db = Firebase.firestore
        val postCollection = db.collection("Posts")

        val currUser = auth.currentUser?.uid

        if (currUser != null) {
            postCollection.document(postID).collection("Likes").document(currUser)
                .delete()
                .addOnSuccessListener {
                    completion()
                }
        }
    }

    fun getPostCaption(postID: String, completion: (String) -> Unit) {
        val db = Firebase.firestore
        val postCollection = db.collection("Posts")

        postCollection.document(postID)
            .get()
            .addOnSuccessListener {
                val theCap = it.get("caption")
                completion(theCap.toString())
            }

            .addOnFailureListener {
                completion("")
            }
    }

    fun getTotalWeightLifted(groupID: String, completion: (Int) -> Unit) {
        val db = Firebase.firestore
        val groupBoardRef = db.collection("Groups")
            .document(groupID)
            .collection("Leaderboards")
            .document("TotalWeightLifted")

        groupBoardRef.get()
            .addOnSuccessListener {
                val theTotal = it.get("totalWeight")
                completion(theTotal.toString().toIntOrNull() ?: 0)
            }

            .addOnFailureListener {
                completion(0)
            }
    }

    fun getTotalWeightLost(groupID: String, completion: (Int) -> Unit) {
        val db = Firebase.firestore
        val groupBoardRef = db.collection("Groups")
            .document(groupID)
            .collection("Leaderboards")
            .document("TotalWeightLost")

        groupBoardRef.get()
            .addOnSuccessListener {
                val theTotal = it.get("totalWeight")
                completion(theTotal.toString().toIntOrNull() ?: 0)
            }

            .addOnFailureListener {
                completion(0)
            }
    }

    fun getTotalWeightGained(groupID: String, completion: (Int) -> Unit) {
        val db = Firebase.firestore
        val groupBoardRef = db.collection("Groups")
            .document(groupID)
            .collection("Leaderboards")
            .document("TotalWeightGained")

        groupBoardRef.get()
            .addOnSuccessListener {
                val theTotal = it.get("totalWeight")
                completion(theTotal.toString().toIntOrNull() ?: 0)
            }

            .addOnFailureListener {
                completion(0)
            }
    }

    fun getGroupInvite(groupID: String, completion: (String) -> Unit,) {
        val db = Firebase.firestore
        val groupRef = db.collection("Groups").document(groupID)

        groupRef.get()
            .addOnSuccessListener {
                val theInviteCode = it.get("inviteCode")

                completion(theInviteCode.toString())
            }

            .addOnFailureListener {

            }
    }

    fun leaveGroup(groupID: String, completion: () -> Unit) {
        val db = Firebase.firestore
        val groupUserRef = db.collection("Groups").document(groupID).collection("Users")

        val currUser = auth.currentUser?.uid

        val userGroupsRef = currUser?.let { db.collection("Users").document(it).collection("Groups") }

        groupUserRef.whereEqualTo("uuid", currUser)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Delete the document
                    groupUserRef.document(document.id).delete()
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener { exception ->
                            // Handle any errors
                            println("Error deleting document: $exception")
                        }
                }

                userGroupsRef?.whereEqualTo("guuid", groupID)?.get()
                    ?.addOnSuccessListener { documents ->
                        for (document in documents) {
                            userGroupsRef.document(document.id).delete()
                                .addOnSuccessListener {
                                    completion()
                                }
                        }
                    }



            }
            .addOnFailureListener { exception ->
                // Handle any errors
                println("Error getting documents: $exception")
            }
        }


}