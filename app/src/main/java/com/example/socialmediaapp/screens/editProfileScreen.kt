package com.example.socialmediaapp.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.components.Header
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.ThreeDotsMenu
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.bioField
import com.example.socialmediaapp.components.editGroupPhoto
import com.example.socialmediaapp.components.editPfp
import com.example.socialmediaapp.components.editPfpCircle
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.myViewModel
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.textField
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.editprofileViewModel

@OptIn(UnstableApi::class)
@Composable
fun EditProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit = {},
    navController: NavController
) {
    var userBio by remember {
        mutableStateOf("")
    }

    var username by remember {
        mutableStateOf("")
    }

    var dataReady by remember {
        mutableStateOf(false)
    }

    val dbCalls = databaseCalls("")

    val theContext = LocalContext.current

    val editViewModel = editprofileViewModel()

    var profilePicUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            profilePicUri.value = uri
            editViewModel.setPfp(profilePicUri.value)
        }
    )

    LaunchedEffect(true) {
        dbCalls.getUserBio { theBio ->
            userBio = theBio

            dbCalls.getUsername { theUsername ->
                username = theUsername

                dbCalls.getPfp {userUri ->
                    profilePicUri.value = userUri

                    dataReady = true
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradientGrey)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(myGradientGrey)
        ) {

            if (!dataReady) {
                CircularProgressIndicator(color = offWhiteBack)
            } else {

                editViewModel.setUsername(username)
                editViewModel.setBio(userBio)
                editViewModel.setOldPfp(profilePicUri.value)
                editViewModel.setPfp(profilePicUri.value)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp, start = 28.dp, end = 28.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        backButton(thisOnClick = {
                            dbCalls.applyProfileChanges(editViewModel, theContext) {
                                navController.navigate("Profile")
                            }
                    })

                    Spacer(modifier = Modifier.weight(1f))

                    ThreeDotsMenu(onMenuItemClick = {
                        if (it == "Logout") {
                            dbCalls.applyProfileChanges(editViewModel, theContext) {
                                onSignOut()
                            }
                        }

                        if (it == "DELETE") {

                            dbCalls.getCurrUser {
                                onSignOut()
                                dbCalls.deleteAccount(it) {
                                    Toast.makeText(
                                        theContext,
                                        "Account Deleted!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    })
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Header(value = "Profile")
                }
            }

            Spacer(modifier = Modifier.padding(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    ),
                //contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-40).dp)

                ) {
                    editPfp(profilePicUri) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                )
                {
                    Spacer(modifier = Modifier.padding(30.dp))
                    textField(labelValue = username, viewModel = editViewModel)
                    Spacer(modifier = Modifier.padding(10.dp))
                    bioField(labelValue = userBio, viewModel = editViewModel)
                }
            }
        }

    }
}
}

@Preview
@Composable
fun defPrev() {
    EditProfileScreen(navController = rememberNavController(), userData = null)
}