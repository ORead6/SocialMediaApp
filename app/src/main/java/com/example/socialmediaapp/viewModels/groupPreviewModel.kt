package com.example.socialmediaapp.viewModels


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class groupPreviewModel() : ViewModel(){

    private val auth = Firebase.auth

    private val _groupID = mutableStateOf("")
    val groupID: State<String> = _groupID

    fun setGroupID(text: String) {
        _groupID.value = text
    }

    fun addWeightToDBLifted(
        weightToBeAdded: String,
        theContext: Context,
        completion: () -> Unit
    ) {
        val isInt = weightToBeAdded.toIntOrNull()?.let { it.toString() == weightToBeAdded } ?: false

        if (!isInt) {
            Toast.makeText(theContext, "Weight to Be added Must be Integer!", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val addWeight = weightToBeAdded.toInt()

        if (addWeight <= 0) {
            Toast.makeText(theContext, "Weight Added must be Larger than 0!", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val currUser = auth.currentUser?.uid ?: run {
            Toast.makeText(theContext, "Error: Current user is null", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val db = Firebase.firestore
        val groupBoardRef = db.collection("Groups").document(groupID.value).collection("Leaderboards")
            .document("TotalWeightLifted")

        // Fetch the required documents
        val userWeightRef = groupBoardRef.collection("Users").document(currUser)

        db.runTransaction { transaction ->
            val groupBoardDoc = transaction.get(groupBoardRef)
            val userWeightDoc = transaction.get(userWeightRef)

            val theTotal = groupBoardDoc.getLong("totalWeight") ?: 0
            val newTotal = theTotal + addWeight

            if (userWeightDoc.exists()) {
                transaction.update(groupBoardRef, "totalWeight", newTotal)
            } else {
                transaction.set(groupBoardRef, mapOf("totalWeight" to newTotal))
            }

            val userTotal = userWeightDoc.getLong("userTotalWeight") ?: 0
            val newUserTotal = userTotal + addWeight

            if (userWeightDoc.exists()) {
                transaction.update(userWeightRef, "userTotalWeight", newUserTotal)
            } else {
                transaction.set(userWeightRef, mapOf("userTotalWeight" to newUserTotal))
            }

            // Return the new total weight
            newTotal
        }.addOnSuccessListener { newTotal ->
            Toast.makeText(theContext, "Weight Added! Well Done!", Toast.LENGTH_SHORT).show()
            completion()
        }.addOnFailureListener { exception ->
            Toast.makeText(theContext, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            completion()
        }
    }

    fun addWeightToDBGained(
        weightToBeAdded: String,
        theContext: Context,
        completion: () -> Unit
    ) {
        val isInt = weightToBeAdded.toIntOrNull()?.let { it.toString() == weightToBeAdded } ?: false

        if (!isInt) {
            Toast.makeText(theContext, "Weight to Be added Must be Integer!", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val addWeight = weightToBeAdded.toInt()

        if (addWeight <= 0) {
            Toast.makeText(theContext, "Weight Added must be Larger than 0!", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val currUser = auth.currentUser?.uid ?: run {
            Toast.makeText(theContext, "Error: Current user is null", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val db = Firebase.firestore
        val groupBoardRef = db.collection("Groups").document(groupID.value).collection("Leaderboards")
            .document("TotalWeightGained")

        // Fetch the required documents
        val userWeightRef = groupBoardRef.collection("Users").document(currUser)

        db.runTransaction { transaction ->
            val groupBoardDoc = transaction.get(groupBoardRef)
            val userWeightDoc = transaction.get(userWeightRef)

            val theTotal = groupBoardDoc.getLong("totalWeight") ?: 0
            val newTotal = theTotal + addWeight

            if (userWeightDoc.exists()) {
                transaction.update(groupBoardRef, "totalWeight", newTotal)
            } else {
                transaction.set(groupBoardRef, mapOf("totalWeight" to newTotal))
            }

            val userTotal = userWeightDoc.getLong("userTotalWeight") ?: 0
            val newUserTotal = userTotal + addWeight

            if (userWeightDoc.exists()) {
                transaction.update(userWeightRef, "userTotalWeight", newUserTotal)
            } else {
                transaction.set(userWeightRef, mapOf("userTotalWeight" to newUserTotal))
            }

            // Return the new total weight
            newTotal
        }.addOnSuccessListener { newTotal ->
            Toast.makeText(theContext, "Weight Added! Well Done!", Toast.LENGTH_SHORT).show()
            completion()
        }.addOnFailureListener { exception ->
            Toast.makeText(theContext, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            completion()
        }
    }

    fun addWeightToDBLost(
        weightToBeAdded: String,
        theContext: Context,
        completion: () -> Unit
    ) {
        val isInt = weightToBeAdded.toIntOrNull()?.let { it.toString() == weightToBeAdded } ?: false

        if (!isInt) {
            Toast.makeText(theContext, "Weight to Be added Must be Integer!", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val addWeight = weightToBeAdded.toInt()

        if (addWeight <= 0) {
            Toast.makeText(theContext, "Weight Added must be Larger than 0!", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val currUser = auth.currentUser?.uid ?: run {
            Toast.makeText(theContext, "Error: Current user is null", Toast.LENGTH_SHORT).show()
            completion()
            return
        }

        val db = Firebase.firestore
        val groupBoardRef = db.collection("Groups").document(groupID.value).collection("Leaderboards")
            .document("TotalWeightLost")

        // Fetch the required documents
        val userWeightRef = groupBoardRef.collection("Users").document(currUser)

        db.runTransaction { transaction ->
            val groupBoardDoc = transaction.get(groupBoardRef)
            val userWeightDoc = transaction.get(userWeightRef)

            val theTotal = groupBoardDoc.getLong("totalWeight") ?: 0
            val newTotal = theTotal + addWeight

            if (userWeightDoc.exists()) {
                transaction.update(groupBoardRef, "totalWeight", newTotal)
            } else {
                transaction.set(groupBoardRef, mapOf("totalWeight" to newTotal))
            }

            val userTotal = userWeightDoc.getLong("userTotalWeight") ?: 0
            val newUserTotal = userTotal + addWeight

            if (userWeightDoc.exists()) {
                transaction.update(userWeightRef, "userTotalWeight", newUserTotal)
            } else {
                transaction.set(userWeightRef, mapOf("userTotalWeight" to newUserTotal))
            }

            // Return the new total weight
            newTotal
        }.addOnSuccessListener { newTotal ->
            Toast.makeText(theContext, "Weight Added! Well Done!", Toast.LENGTH_SHORT).show()
            completion()
        }.addOnFailureListener { exception ->
            Toast.makeText(theContext, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            completion()
        }
    }

}
