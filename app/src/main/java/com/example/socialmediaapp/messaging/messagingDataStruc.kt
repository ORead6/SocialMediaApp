package com.example.socialmediaapp.messaging

import com.google.firebase.Timestamp

data class messagingDataStruc(
    var senderID: String = "",
    var receiverID: String = "",
    var msg: String = "",
    var timestamp: Timestamp = Timestamp.now()
) {
    //no argument constructor
    constructor() : this("", "", "", Timestamp.now())
}