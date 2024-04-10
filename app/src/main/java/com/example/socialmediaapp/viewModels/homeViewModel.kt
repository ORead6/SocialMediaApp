package com.example.socialmediaapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
class homeViewModel() : ViewModel(){
    fun swipe(direction: String, context: Context, currPostIndex: Int, postIds: MutableList<String>, completion: (Int) -> Unit) {


        if (currPostIndex != 0 && direction == "DOWN") {
            // Carry out down
            val value = -1
            completion((currPostIndex + value))

        }

        if (currPostIndex != (postIds.size - 1) && direction == "UP") {
            // Carry out UP
            val value = 1
            completion((currPostIndex + value))
        }

        else {
            // Reached end of Videos
            if (direction == "UP" && currPostIndex == (postIds.size - 1)) {
                Toast.makeText(context, "You have hit the end of your queue", Toast.LENGTH_SHORT).show()
            }
            if (direction == "DOWN" && currPostIndex == 0){
                Toast.makeText(context, "You have hit the top of your queue", Toast.LENGTH_SHORT).show()
            }
        }

    }


}
