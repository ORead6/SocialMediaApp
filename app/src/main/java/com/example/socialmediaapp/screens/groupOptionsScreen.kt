package com.example.socialmediaapp.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.components.Header
import com.example.socialmediaapp.components.ThreeDotsMenu
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.editPfp
import com.example.socialmediaapp.components.groupBioField
import com.example.socialmediaapp.components.groupNameField
import com.example.socialmediaapp.components.groupOptionsPhoto
import com.example.socialmediaapp.components.myCustomFont
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.privacyButton
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.viewModels.groupOptionsViewModel
import kotlinx.coroutines.delay

@Composable
fun groupOptionsScreen(
    groupID: String,
    navController: NavHostController,
) {
    val dbCalls = databaseCalls("")
    var groupName by remember { mutableStateOf("") }
    var groupBio by remember { mutableStateOf("") }
    var privStatus by remember { mutableStateOf(false) }
    var dataReady by remember { mutableStateOf(false) }
    var count by remember { mutableIntStateOf(0) }

    val myViewModel = groupOptionsViewModel()

    val theContext = LocalContext.current

    var groupPhoto = remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            groupPhoto.value = uri
            myViewModel.setPhoto(groupPhoto.value)
        }
    )

    LaunchedEffect(Unit) {
        dbCalls.getGroupName(groupID) {theName ->
            groupName = theName

            dbCalls.getGroupDesc(groupID) {theDesc ->
                groupBio = theDesc

                dbCalls.getGroupPrivacy(groupID) {status ->
                    if (status == "true") {
                        privStatus = true
                    }

                    dbCalls.getGroupPhoto(groupID) {
                        groupPhoto.value = it

                        dataReady = true
                    }


                }

            }

        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = myGradientGrey)
    ) {

        if (dataReady) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = myGradientGrey)
            ) {

                myViewModel.setName(groupName)
                myViewModel.setDesc(groupBio)
                myViewModel.setPriv(privStatus)

                if (myViewModel.oldGroupPhoto.value == null && count == 0) {
                    count++
                    myViewModel.setOldPhoto(groupPhoto.value)
                }

                myViewModel.setPhoto(groupPhoto.value)

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

                            if (myViewModel.groupName.value.isEmpty() ||
                                myViewModel.groupDesc.value.isEmpty()) {
                                Toast.makeText(theContext, "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show()
                            } else {
                                dbCalls.applyGroupChanges(groupID, myViewModel.groupName.value, myViewModel.groupDesc.value, myViewModel.groupPriv.value,
                                    myViewModel.oldGroupPhoto.value, myViewModel.groupPhoto.value) {
                                    if (it) {
                                        Toast.makeText(theContext, "Changes Successfully Made", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(theContext, "Changes Failed", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                }
                            }
                        })

                        Spacer(modifier = Modifier.weight(1f))
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Header(value = "Group Options")
                    }
                }

                Spacer(Modifier.padding(12.dp))

                Column(modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    groupOptionsPhoto(groupPhoto) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }

                Spacer(Modifier.padding(8.dp))

                groupNameField(labelValue = groupName, myViewModel)

                Spacer(Modifier.padding(8.dp))

                groupBioField(labelValue = groupBio, viewModel = myViewModel)

                Spacer(Modifier.padding(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "Privacy Status", style = TextStyle(
                            color = offWhiteBack,
                            fontFamily = myCustomFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ))

                        privacyButton(status = privStatus) {
                            privStatus = !privStatus
                            myViewModel.setPriv(privStatus)
                        }


                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(color = offWhiteBack)
            }
        }

    }
}


@Preview
@Composable
fun previewGroupOptScreen() {
    groupOptionsScreen(groupID = "ABCTest", navController = rememberNavController())
}