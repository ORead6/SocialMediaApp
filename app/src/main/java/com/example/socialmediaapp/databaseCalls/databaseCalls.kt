package com.example.socialmediaapp.databaseCalls

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.socialmediaapp.messaging.messagingDataStruc
import com.example.socialmediaapp.viewModels.editprofileViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.random.Random

class databaseCalls (
    private val userId: String
)
{
    private val auth = Firebase.auth

    fun getPosts(userID: String = "", completion: (List<String>) -> Unit) {
        val dbReference = Firebase.firestore
        var currentUserUUID = ""

        if (userID == "") {
            currentUserUUID = auth.currentUser?.uid.toString()
        } else {
            currentUserUUID = userID
        }

        val docRef = dbReference.collection("Posts")
        val postIds = mutableListOf<String>()

        docRef.whereEqualTo("createdBy", currentUserUUID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id
                    postIds.add(documentId)
                }
                completion(postIds)
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
            .orderBy("uploadedAt", Query.Direction.DESCENDING)
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
                Log.d("GROUP MEDIA", it.toString())
            }
    }

    fun getGroups(userID: String = "", completion: (List<String>) -> Unit) {
        val dbReference = Firebase.firestore
        val groupIds = mutableListOf<String>()

        var currentUserUUID = ""

        if (userID == "") {
            currentUserUUID = auth.currentUser?.uid.toString()
        } else {
            currentUserUUID = userID
        }

        dbReference.collection("Groups").get()
            .addOnSuccessListener { groups ->
                val tasks = mutableListOf<Task<QuerySnapshot>>()
                val groupList = groups.documents.toList()

                for (group in groupList) {
                    val groupId = group.id
                    val usersRef =
                        dbReference.collection("Groups").document(groupId).collection("Users")
                    val userQuery = usersRef.whereEqualTo("uuid", currentUserUUID).get()

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

    fun getGroupNames(completion: (List<String>) -> Unit) {
        val dbReference = Firebase.firestore
        val groupIds = mutableListOf<String>()

        val groupNames = mutableListOf<String>()

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


                        for (id in groupIds) {
                            getGroupName(id) {name ->
                                groupNames.add(name)

                                if (groupNames.size == groupIds.size) {
                                    completion(groupNames)
                                }
                            }
                        }



                    }
                    .addOnFailureListener {
                        // Handle failure
                    }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun getGroupIdWithName(groupNameSelection: String, completion: (String) -> Unit) {
        val currentUserUUID = auth.currentUser?.uid

        val db = Firebase.firestore
        val groupCollection = db.collection("Groups")

        groupCollection.whereEqualTo("groupName", groupNameSelection)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // If no documents match the query, return null
                    completion("")
                } else {
                    // Assuming there is only one document with the specified groupName
                    val groupId = documents.documents.first().id
                    completion(groupId)
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
                Log.d("Image Retreival Error", "HERE")
                e.printStackTrace()
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

    fun getGroupInvite(groupID: String, context: Context, completion: (String) -> Unit,) {
        val db = Firebase.firestore
        val groupRef = db.collection("Groups").document(groupID)

        groupRef.get()
            .addOnSuccessListener {

                val privacyStatus = it.get("privacyStatus")

                if(privacyStatus != "true") {
                    val theInviteCode = it.get("inviteCode")

                    completion(theInviteCode.toString())
                }

                else {
                    Toast.makeText(context, "Group is set to private", Toast.LENGTH_SHORT).show()
                }

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

    fun getOwnerStatus(groupID: String, completion: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val groupRef = db.collection("Groups").document(groupID)

        val currUser = auth.currentUser?.uid ?: ""

        groupRef.get()
            .addOnSuccessListener {
                val theOwner = it.get("owner")

                if (theOwner.toString() == currUser) {
                    // The user Logged in is Owner
                    completion(true)
                } else {
                    // User is just a member
                    completion(false)
                }
            }
    }

    fun deleteAccount(currUser: FirebaseUser?, completion: () -> Unit) {
        val auth = FirebaseAuth.getInstance()

        currUser?.delete()
            ?.addOnSuccessListener {
                completion()
            }
    }

    fun getCurrUser(completion: (FirebaseUser?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        completion(user)

    }

    fun getGroupUsers(groupID: String, completion: (List<String>) -> Unit) {
        val db = Firebase.firestore
        val groupRef = db.collection("Groups").document(groupID)

        val listOfIds = mutableListOf<String>()

        groupRef.collection("Users")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val userID = document.get("uuid")

                    listOfIds.add(userID.toString())
                }

                completion(listOfIds)
            }
    }

    fun getUsernamesWithIDS(userIds: MutableList<String>, completion: (Map<String, String>) -> Unit) {
        val db = Firebase.firestore
        val users = db.collection("Users")

        val resultMap = mutableMapOf<String, String>()
        var processedCount = 0

        for (id in userIds) {
            users.document(id).get()
                .addOnSuccessListener { documentSnapshot ->
                    val username = documentSnapshot.getString("username")
                    if (username != null) {
                        resultMap[id] = username
                    }
                    processedCount++

                    if (processedCount == userIds.size) {
                        completion(resultMap)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    exception.printStackTrace()
                    processedCount++  // Ensure processed count is incremented even in case of failure

                    if (processedCount == userIds.size) {
                        completion(resultMap)
                    }
                }
        }
    }

    fun getMetricValuesWithUserIds(metric: String, groupID: String, userIDs: List<String>, completion: (Map<String, String>) -> Unit) {
        val db = Firebase.firestore
        val groupLeaderboard = db.collection("Groups").document(groupID).collection("Leaderboards").document(metric).collection("Users")

        val resultMap = mutableMapOf<String, String>()
        var processedCount = 0

        for (id in userIDs) {
            groupLeaderboard.document(id).get()
                .addOnSuccessListener {
                    val value = it.get("userTotalWeight")
                    if (value != null) {
                        resultMap[id] = value.toString()
                    } else {
                        resultMap[id] = "0"
                    }

                    processedCount++

                    if (processedCount == userIDs.size) {
                        completion(resultMap)
                    }
                }

                .addOnFailureListener { exception ->
                    // Handle failure
                    exception.printStackTrace()
                    processedCount++  // Ensure processed count is incremented even in case of failure

                    if (processedCount == userIDs.size) {
                        completion(resultMap)
                    }
                }
        }
    }

    fun createMyUsers() {
        // Create User Docs
        val uuidList = mutableListOf<UUID>()
        repeat(1) {
            uuidList.add(UUID.randomUUID())
        }

        val prefixes = listOf("Al", "Be", "Ce", "Da", "El", "Fa", "Ga", "Ha", "I", "Jo", "Ka", "La", "Ma", "Na", "O", "Pa", "Qu", "Ra", "Se", "Te", "U", "Va", "Wa", "Xa", "Y", "Za")
        val suffixes = listOf("ara", "bel", "cia", "dal", "el", "fia", "gil", "hil", "ina", "ja", "kia", "la", "mia", "na", "ola", "pa", "qua", "ra", "sa", "ta", "ula", "va", "wa", "xa", "ya", "za")

        val db = Firebase.firestore
        val userColl = db.collection("Users")
        val groupColl = db.collection("Groups").document("SRuKfA9Pdyd73QcQW8St")

        for (id in uuidList) {
            val randomPrefix = prefixes.random()
            val randomSuffix = suffixes.random()
            val userData = hashMapOf<String, String>(
                "username" to (randomPrefix + randomSuffix)
            )

            userColl.document(id.toString()).set(userData)
                .addOnSuccessListener {

                    val userIDData = hashMapOf<String, String>(
                        "uuid" to id.toString()
                    )
                    groupColl.collection("Users").add(userIDData)
                        .addOnSuccessListener {


                            val liftData = hashMapOf<String, Int>(
                                "userTotalWeight" to (Random.nextInt(10, 100 + 1))
                            )

                            val lostData = hashMapOf<String, Int>(
                                "userTotalWeight" to (Random.nextInt(1, 20 + 1))
                            )

                            val gainData = hashMapOf<String, Int>(
                                "userTotalWeight" to (Random.nextInt(1, 20 + 1))
                            )

                            groupColl.collection("Leaderboards")
                                .document("TotalWeightGained")
                                .collection("Users")
                                .document(id.toString()).set(gainData)
                                .addOnSuccessListener {

                                    groupColl.collection("Leaderboards")
                                        .document("TotalWeightLost")
                                        .collection("Users")
                                        .document(id.toString()).set(lostData)
                                        .addOnSuccessListener {

                                            groupColl.collection("Leaderboards")
                                                .document("TotalWeightLifted")
                                                .collection("Users")
                                                .document(id.toString()).set(liftData)
                                                .addOnSuccessListener {

                                                }

                                        }

                                }

                        }

                }
        }
    }

    //NEED TO ADD
    // MAKING SURE THE USERS HAVE NOT SEEN THE VIDEOS ALREADY
    fun getVideos(videoCount: Int, completion: (List<String>) -> Unit) {
        val db = Firebase.firestore
        val postColl = db.collection("Posts")
        val userID = auth.currentUser?.uid

        val postIDs = mutableListOf<String>()

        val query = postColl
            .whereEqualTo("mediaType", "video")
            .whereEqualTo("postedTo", "")
            .limit(videoCount.toLong())
            .orderBy("uploadedAt", Query.Direction.DESCENDING)

        query.get()
            .addOnSuccessListener {documents ->
                for (document in documents) {
                    // Extract the values of the fields needed for additional filtering
                    val mediaType = document.getString("mediaType")
                    val createdBy = document.getString("createdBy")

                    // Perform additional filtering client-side
                    if (mediaType == "video" && createdBy != userID) {
                        var theID = document.id
                        postIDs.add(theID)
                    }
                }
                completion(postIDs)
            }

            .addOnFailureListener { exception ->
                println("Failed to fetch documents: $exception")
                completion(postIDs)
            }
    }

    fun getAllUris(postIds: MutableList<String>, completion: (Map<String, Uri>) -> Unit) {
        val uriMap = mutableMapOf<String, Uri>()
        val storage = FirebaseStorage.getInstance()
        var downloadCount = 0

        for (id in postIds) {
            val fileRef = storage.reference.child("Posts/${id}/postMedia")

            fileRef.downloadUrl
                .addOnSuccessListener { uri ->
                    uriMap[id] = uri
                    downloadCount++
                    if (downloadCount == postIds.size) {
                        // All videos have been downloaded
                        completion(uriMap)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("Image Retreival Error", e.toString())
                    downloadCount++
                    if (downloadCount == postIds.size) {
                        // All videos have been downloaded (even if some failed)
                        completion(uriMap)
                    }
                }
        }
    }

    fun getVideoCaptions(postIds: MutableList<String>, completion: (Map<String, String>) -> Unit) {
        val db = Firebase.firestore
        val postColl = db.collection("Posts")

        val captionMap = mutableMapOf<String, String>()

        var progressCounter = 0

        for (id in postIds) {
            postColl.document(id)
                .get()
                .addOnSuccessListener {
                    val caption = it.get("caption")
                    captionMap[id] = caption.toString()
                    progressCounter++
                    if (progressCounter == postIds.size) {
                        completion(captionMap)
                    }
                }
                .addOnFailureListener {
                    progressCounter++
                    if (progressCounter == postIds.size) {
                        completion(captionMap)
                    }
                }
        }

    }

    fun getUserWeights(completion: (Map<String, Any>) -> Unit) {
        val db = Firebase.firestore
        val weightMap = mutableMapOf<String, Any>()
        val currUser = auth.currentUser?.uid

        if (currUser != null) {
            val userWeightRef = db.collection("Users")
                .document(currUser)
                .collection("Weights")
                .document("userWeights")

            userWeightRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val data = documentSnapshot.data
                        if (data != null) {
                            weightMap.putAll(data)
                            completion(weightMap)
                        } else {
                            completion(weightMap)
                        }
                    } else {
                        // Handle case where document does not exist
                        completion(weightMap)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Log.d("WEIGHTFAIL", exception.toString())
                    completion(weightMap)
                }
        }
    }

    fun getAdditionalVideo(postIds: MutableList<String>, completion: (String) -> Unit) {
        val db = Firebase.firestore
        val postColl = db.collection("Posts")

        // Query for documents not in postIds list
        postColl.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                // Get all document IDs from the query result
                val allDocumentIds = querySnapshot.documents.map { it.id }

                // Filter out IDs that are not in postIds list
                val filteredIds = allDocumentIds.filterNot { postId -> postIds.contains(postId) }

                // Choose a random ID from filteredIds list
                val randomId = filteredIds.randomOrNull()

                if (randomId != null) {
                    completion(randomId)
                }
            }
    }

    fun orderOnWeights(
        postIds: MutableList<String>,
        videoCapt: MutableMap<String, String>,
        userWeights: MutableMap<String, Long>,
        currPostIndex: Int,
        completion: (List<String>) -> Unit
    ) {

        val wordMap  = mutableMapOf<String, List<String>>()

//        // TMP
//        var mylist = mutableListOf("A",
//            "B",
//            "C",
//            "D",
//            "E"
//        )
//
//        var weightMap = mutableMapOf(
//            "seven" to 1,
//            "ten" to 2,
//            "twelve" to 3,
//            "fourteen" to 1,
//            "fifteen" to 1,
//        )
//
//        var captMap = mutableMapOf<String, String>(
//            "A" to "One Two Three",
//            "B" to "Four Five Six",
//            "C" to "Seven Eight Nine",
//            "D" to "#Ten Eleven twelve#",
//            "E" to "Thirteen Fourteen #Fifteen"
//        )
//        TMP
//        val currIndex = 1


        val captWeightMap = mutableMapOf<String, Double>()
        for (key in postIds) {
            captWeightMap[key] = 0.0
        }


        val watchedVids = mutableListOf<String>()
        val newVids = mutableListOf<String>()


        // Watched Vids
        for (i in 0..currPostIndex) {
            if (i < postIds.size) {
                watchedVids.add(postIds[i])
            }
        }

        // Split into unwatch videos
        for (i in (currPostIndex + 1) until postIds.size) {
            newVids.add(postIds[i])
        }

        // Go through capt
        for (id in newVids) {

            val capt = videoCapt[id]

            val words = capt
                ?.lowercase()
                ?.replace("[^a-zA-Z0-9#\\s]".toRegex(), "")
                ?.split("\\s+".toRegex())

            val updatedWords = words?.map { word ->
                if (word.endsWith("#")) {
                    word.substring(0, word.length - 1) // Remove the hashtag at the end of the word
                } else {
                    word
                }
            }

            val nonNullableUpdatedWords = updatedWords ?: listOf()
            wordMap[id] = nonNullableUpdatedWords
        }

        // Loop through to find avg weight of each
        for ((key, value) in wordMap) {
            var totalWeight = 0
            var thisWeight = 0
            var multiplier = 1

            for (i in value) {

                var currWord = i

                if (currWord.contains("#")) {
                    multiplier = 2
                    currWord = currWord.replace("#", "")
                }

                if (userWeights[currWord] !=  null) {
                    thisWeight = ((userWeights[currWord]!! * multiplier).toInt())
                } else {
                    thisWeight = 0
                }

                totalWeight += thisWeight

                multiplier = 0
            }



            captWeightMap[key] = ((totalWeight.toDouble() / value.size.toDouble()))
        }

        // Order newVids based on their key value in captWeightMap
        newVids.sortByDescending { captWeightMap[it] }

        watchedVids.addAll(newVids)

        completion(watchedVids)

    }

    fun interactionChange(caption: String, userWeights: MutableMap<String, Long>, type: String, completion: (Map<String, Long>) -> Unit) {
        val db = Firebase.firestore
        val currUser = auth.currentUser?.uid

        // Get formatted caption
        val words = formatCaption(caption)

        if (words != null) {
            for (word in words) {
                if (type == "+") {
                    val currentWeight = userWeights[word] ?: 0
                    userWeights[word] = currentWeight + 1
                } else {
                    val currentWeight = userWeights[word] ?: 0
                    userWeights[word] = maxOf(0, currentWeight - 1)
                }

            }

            // Update the DB
            if (currUser != null) {
                val userWeightRef = db.collection("Users")
                    .document(currUser)
                    .collection("Weights")
                    .document("userWeights")

                userWeightRef.set(userWeights, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d("POSTINTERACTION", "NEW MAP:  $userWeights")
                        completion(userWeights)
                    }
                    .addOnFailureListener {
                        Log.d("POSTINTERACTION", "Couldnt Merge: ${it.toString()}")
                        completion(userWeights)
                    }
            }


        } else {
            completion(userWeights)
        }

    }

    fun formatCaption(caption: String): List<String>? {
        // remove common words that wont affect weighting
        // Can review common words after large training and add them in
        val commonWords = listOf(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "the", "be", "to", "of", "and", "in", "that", "have", "it", "for", "not", "is", "on", "with", "he", "as", "you", "do", "at", "this",
            "but", "his", "by", "from", "they", "we", "say", "her", "she", "or", "an", "will", "my", "one", "all", "would", "there", "their",
            "what", "so", "up", "out", "if", "about", "who", "get", "which", "go", "me", "when", "make", "can", "like", "time", "no", "just",
            "him", "know", "take", "people", "into", "year", "your", "good", "some", "could", "them", "see", "other", "than", "then", "now",
            "look", "only", "come", "its", "over", "think", "also", "back", "after", "use", "two", "how", "our", "work", "first", "well",
            "way", "even", "new", "want", "because", "any", "these", "give", "day", "most", "us"
        )

        val words = caption
            .replace("[^a-zA-Z0-9#\\s]".toRegex(), "")
            .lowercase()
            .split("\\s+".toRegex())
            .filter { word ->
                word !in commonWords && !word.any { it.isLetterOrDigit().not() }
            }

        return words
    }

    /**
     * Take User ID, if null use logged in user
     * if not null use that user ID
     * Find User document in database and collect the field 'bio'
     */
    fun getUserBio(userID: String = "", completion: (String) -> Unit) {
        val db = Firebase.firestore
        var currUser = ""

        // get UserID
        if (userID == "") {
            currUser = auth.currentUser?.uid.toString()
        } else {
            currUser = userID
        }


        if (currUser != null) {
            // Get Referenced Doc
            val userRef = db.collection("Users").document(currUser)
            userRef.get()
                .addOnSuccessListener {
                    val theBio = it.get("bio")

                    if (theBio != null){
                        completion(theBio.toString())
                    } else {
                        completion("")
                    }
                }

        } else {
            completion("")
        }

    }

    fun applyProfileChanges(
        context: Context,
        username: String,
        bio: String,
        newPfp: Uri?,
        oldPfp: Uri?,
        completion: () -> Unit
    ) {
        val storageRef = FirebaseStorage.getInstance().reference
        val db = Firebase.firestore

        val auth = Firebase.auth
        val currUser = auth.currentUser?.uid

        if (username == "") {
            Toast.makeText(context, "Username Can not be Blank", Toast.LENGTH_SHORT).show()
        } else {
            if (currUser != null) {
                val updates = hashMapOf<String, Any>(
                    "username" to username,
                    "bio" to bio
                )

                val userRef = db.collection("Users").document(currUser)
                userRef.update(updates)
                    .addOnSuccessListener{
                        if (newPfp != null && newPfp != oldPfp) {
                            val photoRef = storageRef.child("Users/$currUser/pfpPic")
                            photoRef.putFile(newPfp)
                                .addOnSuccessListener {
                                    completion()
                                }
                                .addOnFailureListener{
                                    Log.d("PHOTO_ERROR", it.printStackTrace().toString())
                                    completion()
                                }
                        }
                    }

                    .addOnFailureListener{
                        completion()
                    }
            }
        }
    }

    private fun updateUserDocument(
        userId: String,
        updates: Map<String, Any>,
        completion: () -> Unit
    ) {
        val db = Firebase.firestore
        val userDoc = db.collection("Users").document(userId)
        userDoc.update(updates)
            .addOnSuccessListener {
                completion()
            }
            .addOnFailureListener { exception ->
                Log.e("UPDATE_USER", "Failed to update user document: $exception")
            }
    }

    fun getUsername(userID:String = "", completion: (String) -> Unit) {
        val db = Firebase.firestore
        var currUser = ""

        if (userID == "") {
            currUser = auth.currentUser?.uid.toString()
        } else {
            currUser = userID
        }

        if (currUser != null) {
            val userDoc = db.collection("Users").document(currUser)
            userDoc.get()
                .addOnSuccessListener {
                    val theName = it.get("username")
                    completion(theName.toString())
                }

                .addOnFailureListener {
                    completion("404")
                }
        }
    }

    /**
     * Take User ID, if null use logged in user
     * if not null use that user ID
     * Find User Profile picture, download and return
     * If no picture exists at reference return an empty URI
     */
    fun getPfp(userID: String = "", completion: (Uri?) -> Unit) {

        // get User ID
        var currUser = ""
        if (userID == "") {
            currUser = auth.currentUser?.uid.toString()
        } else {
            currUser = userID
        }

        val storage = FirebaseStorage.getInstance()

        // Get Storage Ref
        val fileRef = storage.reference.child("Users/${currUser}/pfpPic")

        // Download URL and do appropriate return
        fileRef.downloadUrl
            .addOnSuccessListener { uri ->
                completion(uri)
            }
            .addOnFailureListener { e ->
                completion(Uri.EMPTY)
            }
    }

    fun getFollowingStatus(userFollowing: String, completion: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val currUser = auth.currentUser?.uid.toString()

        if (userFollowing != "") {
            val userRef = db.collection("Users").document(currUser).collection("Following")
            userRef.whereEqualTo(FieldPath.documentId(), userFollowing).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        completion(true)
                    } else {
                        completion(false)
                    }


                }

        }
        else {
            completion(false)
        }
    }

    fun addFollowing(userThatIsViewed: String, completion: () -> Unit) {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currUser = auth.currentUser?.uid.toString()

        // Add document to the "Following" collection of the current user
        db.collection("Users").document(currUser).collection("Following").document(userThatIsViewed)
            .set(hashMapOf("followedUserId" to userThatIsViewed))
            .addOnSuccessListener {
                // Document added successfully to the "Following" collection
                addFollower(userThatIsViewed, currUser, completion)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("ADD_FOLLOWING", "Error adding document to 'Following' collection: $exception")
            }
    }

    fun addFollower(userThatIsViewed: String, currUser: String, completion: () -> Unit) {
        val db = Firebase.firestore

        // Add document to the "Followers" collection of the viewed user
        db.collection("Users").document(userThatIsViewed).collection("Followers").document(currUser)
            .set(hashMapOf("followerUserId" to currUser))
            .addOnSuccessListener {
                // Document added successfully to the "Followers" collection
                completion()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("ADD_FOLLOWER", "Error adding document to 'Followers' collection: $exception")
            }
    }

    fun removeFollowing(userThatIsViewed: String, completion: () -> Unit) {
        val db = Firebase.firestore
        val currUser = auth.currentUser?.uid.toString()

        // Remove document from the "Following" collection of the current user
        db.collection("Users").document(currUser).collection("Following").document(userThatIsViewed)
            .delete()
            .addOnSuccessListener {
                // Document removed successfully from the "Following" collection
                removeFollower(userThatIsViewed, currUser, completion)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("REMOVE_FOLLOWING", "Error removing document from 'Following' collection: $exception")
            }
    }

    fun removeFollower(userThatIsViewed: String, currUser: String, completion: () -> Unit) {
        val db = Firebase.firestore

        // Remove document from the "Followers" collection of the viewed user
        db.collection("Users").document(userThatIsViewed).collection("Followers").document(currUser)
            .delete()
            .addOnSuccessListener {
                // Document removed successfully from the "Followers" collection
                completion()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("REMOVE_FOLLOWER", "Error removing document from 'Followers' collection: $exception")
            }
    }

    fun getFollowing(userID: String = "", completion: (Map<String, String>) -> Unit) {
        var thisUser = ""

        var following = mutableMapOf<String, String>()

        if (userID == "") {
            thisUser = auth.currentUser?.uid.toString()
        }

        val db = Firebase.firestore
        val userFollowingRef = db.collection("Users").document(thisUser).collection("Following")
        var count = 0

        userFollowingRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    getUsername(id) {
                        val username = it

                        following[username] = id

                        if (count >= documents.size() - 1) {
                            completion(following)
                        }

                        count++
                    }
                }
            }

            .addOnFailureListener {
                completion(emptyMap())
            }


    }

    fun getMessages(secondUser: String, completion: (List<messagingDataStruc>) -> Unit) {
        val thisUser = auth.currentUser?.uid
        val db = Firebase.firestore

        val msgRef = db.collection("Messages")

        val firstQueryTask = msgRef.whereEqualTo("senderID", thisUser)
            .whereEqualTo("receiverID", secondUser)
            .get()

        val secondQueryTask = msgRef.whereEqualTo("senderID", secondUser)
            .whereEqualTo("receiverID", thisUser)
            .get()

        val combinedTask = Tasks.whenAllSuccess<QuerySnapshot>(firstQueryTask, secondQueryTask)

        combinedTask
            .addOnSuccessListener { results ->
                val messages = mutableListOf<messagingDataStruc>()

                // Process results from the first query
                val firstQueryResult = results[0] as QuerySnapshot
                for (document in firstQueryResult) {
                    val message = document.toObject<messagingDataStruc>()
                    messages.add(message)
                }

                // Process results from the second query
                val secondQueryResult = results[1] as QuerySnapshot
                for (document in secondQueryResult) {
                    val message = document.toObject<messagingDataStruc>()
                    messages.add(message)
                }

                // Call completion with the combined messages
                completion(messages)
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("SEARCHBARLIST", "Error getting messages: ", exception)
                completion(emptyList())
            }
    }

    fun sendMessage(newMsg: messagingDataStruc) {
        val thisUser = auth.currentUser?.uid
        val db = Firebase.firestore

        newMsg.senderID = thisUser.toString()

        val msgRef = db.collection("Messages")

        msgRef.add(newMsg)
            .addOnSuccessListener {
                Log.d("MESSAGEDM", newMsg.toString())
            }
    }

    fun getFirstMessages(completion: (List<messagingDataStruc>) -> Unit) {

        val thisUser = auth.currentUser?.uid
        val db = Firebase.firestore

        val msgRef = db.collection("Messages")

        val firstQueryTask = msgRef
            .whereEqualTo("senderID", thisUser)
            .get()

        val secondQueryTask = msgRef
            .whereEqualTo("receiverID", thisUser)
            .get()

        val combinedTask = Tasks.whenAllSuccess<QuerySnapshot>(firstQueryTask, secondQueryTask)

        combinedTask
            .addOnSuccessListener { results ->

                var messages = mutableListOf<messagingDataStruc>()

                var firstMessages = mutableListOf<messagingDataStruc>()

                // Process results from the first query
                val firstQueryResult = results[0] as QuerySnapshot
                for (document in firstQueryResult) {
                    val message = document.toObject<messagingDataStruc>()
                    messages.add(message)
                }

                // Process results from the second query
                val secondQueryResult = results[1] as QuerySnapshot
                for (document in secondQueryResult) {
                    val message = document.toObject<messagingDataStruc>()
                    messages.add(message)
                }

                messages = messages.sortedBy { it.timestamp }.toList().toMutableList()

                val groupedMessages = messages.groupBy { if (it.senderID == thisUser) it.receiverID else it.senderID }

                val latestMessages: List<messagingDataStruc> = groupedMessages
                    .mapValues { (_, messages) ->
                        messages.maxByOrNull { it.timestamp }
                    }
                    .values
                    .filterNotNull()

                completion(latestMessages)

            }
    }

    fun getFollowers(userID: String = "", completion: (Int) -> Unit) {
        var thisUser = ""
        val db = Firebase.firestore

        if (userID == "") {
            thisUser = auth.currentUser?.uid.toString()
        } else {
            thisUser = userID
        }

        var followersRef = db.collection("Users").document(thisUser).collection("Followers")

        followersRef.get()
            .addOnSuccessListener {
                completion(it.size())
            }
    }

    fun getFollowingSize(userID: String = "", completion: (Int) -> Unit) {
        var thisUser = ""
        val db = Firebase.firestore

        if (userID == "") {
            thisUser = auth.currentUser?.uid.toString()
        } else {
            thisUser = userID
        }

        var followersRef = db.collection("Users").document(thisUser).collection("Following")

        followersRef.get()
            .addOnSuccessListener {
                completion(it.size())
            }
    }

    fun getGroupDesc(groupID: String, completion: (String) -> Unit) {
        val db = Firebase.firestore

        val groupRef = db.collection("Groups").document(groupID)

        groupRef.get()
            .addOnSuccessListener {
                val theDesc = it.get("groupBio")

                completion(theDesc.toString())
            }

            .addOnFailureListener {
                completion("")
            }

    }

    fun getGroupPrivacy(groupID: String, completion: (String) -> Unit) {
        val db = Firebase.firestore

        val groupRef = db.collection("Groups").document(groupID)

        groupRef.get()
            .addOnSuccessListener {
                val status = it.get("privacyStatus")

                completion(status.toString())
            }

            .addOnFailureListener {
                completion("false")
            }
    }

    fun applyGroupChanges(groupID: String, name: String, desc: String, priv: Boolean, oldPhoto: Uri?, newPhoto: Uri?, completion: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val groupRef = db.collection("Groups").document(groupID)

        val storageRef = FirebaseStorage.getInstance().reference

        val updates = hashMapOf<String, Any>(
            "groupName" to name,
            "groupBio" to desc,
            "privacyStatus" to priv.toString()
        )

        groupRef.update(updates)
            .addOnSuccessListener {

                Log.d("PHOTOSGroup", newPhoto.toString())
                Log.d("PHOTOSGroup", oldPhoto.toString())
                if (newPhoto != null && newPhoto != oldPhoto) {

                    val photoRef = storageRef.child("Groups/$groupID/groupPhoto.jpg")
                    photoRef.putFile(newPhoto)
                        .addOnSuccessListener { taskSnapshot ->
                            completion(true)
                        }
                        .addOnFailureListener { exception ->
                            completion(true)
                        }

                } else {
                    completion(true)
                }

            }

            .addOnFailureListener {
                completion(false)
            }
    }
}

