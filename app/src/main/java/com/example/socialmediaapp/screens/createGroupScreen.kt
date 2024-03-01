package com.example.socialmediaapp.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.createGroupButton
import com.example.socialmediaapp.components.editGroupPhoto
import com.example.socialmediaapp.components.groupPrivacyButton
import com.example.socialmediaapp.components.header
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.newGroupName
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.privacyText
import com.example.socialmediaapp.viewModels.createGroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

@Composable
fun CreateGroupScreen(navController: NavHostController) {

    val createViewModel = createGroupViewModel()

    val context = LocalContext.current

    var selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = myGradientGrey)
            .padding(top = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    backButton(thisOnClick = {
                        navController.navigate("Groups")
                    })
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    header()
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = offWhiteBack,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
            ) {
                Column (
                    modifier = Modifier.fillMaxSize()
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        editGroupPhoto(selectedImageUri) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        newGroupName(createViewModel)
                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        groupPrivacyButton(createViewModel)
                        privacyText()
                    }

                    // Will have to change this depending on whats built above
                    Spacer(modifier = Modifier.padding(200.dp))

                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp),
                    ) {
                        createGroupButton(thisOnClick = {
                            createViewModel.createNewGroup {groupID ->

                                val storage = FirebaseStorage.getInstance().getReference()

                                // Specify the path to the file in Firebase Storage
                                val path = "Groups/${groupID}/groupPhoto.jpg"

                                Log.d("IMGUPLOAD", path)

                                // Create a Storage reference to the specified path
                                val storageRef = storage.child(path)

                                Log.d("IMGUPLOAD", "Path: ${storageRef.path}")

                                // Upload the file to Firebase Storage
                                storageRef.putFile(selectedImageUri.value!!)
                                    .addOnSuccessListener {
                                        // Handle successful upload
                                        Log.d("IMGUPLOAD", "Image uploaded successfully")
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle failed upload
                                        Log.d("IMGUPLOAD", "Failed to upload image: ${exception.printStackTrace()}")
                                        exception.printStackTrace()
                                    }
                                    .addOnProgressListener { taskSnapshot ->
                                        // You can use this listener to track upload progress if needed
                                        val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                                        Log.d("IMGUPLOAD", "Upload is $progress% done")
                                    }
                            }
                        })
                    }
                }
            }
        }
    }
}


