package com.example.socialmediaapp.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.socialmediaapp.components.mediaDescription
import com.example.socialmediaapp.components.mediaPicker
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.postButton
import com.example.socialmediaapp.components.progressDisplay
import com.example.socialmediaapp.components.uploadHeader
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.uploadViewModel

@Composable
fun uploadMediaScreen(
    userData: UserData?,
    navController: NavController
) {

    val myViewModel = uploadViewModel()

    val dbCalls = databaseCalls(userData?.userId ?: "")

    var selectedMediaUri = remember {
        mutableStateOf<Uri?>(null)
    }

    var groupNames by remember { mutableStateOf(emptyList<String>()) }

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedMediaUri.value = uri
        }
    )

    var uploadingMedia by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select Option*") }
    var newGroupName by remember { mutableStateOf("Public") }

    val context = LocalContext.current

    LaunchedEffect(true) {
        dbCalls.getGroupNames { theGroupNames ->
            groupNames = theGroupNames as MutableList<String>
            Log.d("getGroupNames", "Final group names list: $groupNames")
            groupNames = groupNames.toMutableList().apply {
                add(newGroupName)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = myGradientGrey)
                .padding(20.dp)
        ) {
            uploadHeader()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                mediaPicker {
                    mediaPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                }

                mediaDescription(myViewModel)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(color = Color.White)
                    .clickable { isExpanded = !isExpanded }
            ) {
                Text(text = selectedOption, modifier = Modifier.padding(16.dp))

                DropdownMenu(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(16.dp),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {

                    groupNames.forEach { groupName ->
                        DropdownMenuItem(
                            onClick = {
                                isExpanded = false
                                selectedOption = groupName
                            },
                            text = { Text(text = groupName) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(25.dp))

            if (uploadingMedia) {
                progressDisplay(myViewModel.progressVal.value, "UPLOADING...")
            }

            val height = if (uploadingMedia) { 40.dp } else { 225.dp }

            Spacer(modifier = Modifier.padding(height))
            //   225 - top pad no bar
            // 50  bar

            Box(modifier = Modifier
                .fillMaxWidth()
                ) {

                postButton {
                    if (selectedMediaUri.value != null && selectedOption != "Select Option") {
                        uploadingMedia = true
                        myViewModel.setProgress(0.0)
                        myViewModel.createPost(selectedMediaUri.value, selectedOption, myViewModel,  dbCalls)
                    } else {
                        Toast.makeText(
                            context,
                            "Some Required Fields are Blank",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

        }
    }
}
