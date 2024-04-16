package com.example.socialmediaapp.messaging

import com.google.firebase.Timestamp

data class messagingDataStruc(
    val senderID: String = "",
    val receiverID: String = "",
    val msg: String = "",
    val timestamp: Timestamp = Timestamp.now()
) {
    //no argument constructor
    constructor() : this("", "", "", Timestamp.now())
}