package com.example.socialmediaapp.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.socialmediaapp.components.mediaDescription
import com.example.socialmediaapp.components.mediaPicker
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.uploadHeader
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.uploadViewModel
import okhttp3.internal.wait

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

    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select Option") }

    LaunchedEffect(true) {

        dbCalls.getGroupNames { theGroupNames ->
            groupNames = theGroupNames as MutableList<String>
            Log.d("getGroupNames", "Final group names list: $groupNames")
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
        )
        {
            uploadHeader()

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                mediaPicker {
                    mediaPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                }

                mediaDescription(myViewModel)

            }

        }
    }
}
