package com.example.socialmediaapp.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewModels.createGroupViewModel
import com.example.socialmediaapp.viewModels.groupOptionsViewModel

@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun groupNameField(labelValue: String, viewModel: groupOptionsViewModel) {

    val textValue = remember {
        mutableStateOf(labelValue)
    }

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column (
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Group Name",
                style = TextStyle(
                    fontFamily = myCustomFont,
                    color = offWhiteBack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(20.dp),
            value = textValue.value,
            onValueChange = {
                textValue.value = it
                viewModel.setName(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = textFieldOutline,
                focusedLabelColor = textFieldOutline,
                cursorColor = Primary,
                containerColor = textFieldBG,
                unfocusedBorderColor = textFieldOutline,
//                placeholderColor = Color.LightGray,
//                textColor = darkBG
            ),
            keyboardOptions = KeyboardOptions.Default,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
        )
    }

}

@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun groupBioField(labelValue: String, viewModel: groupOptionsViewModel) {

    val textValue = remember {
        mutableStateOf(labelValue)
    }

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Group Description",
                style = TextStyle(
                    fontFamily = myCustomFont,
                    color = offWhiteBack,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }
        Spacer(Modifier.padding(3.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(20.dp),
            value = textValue.value,
            onValueChange = {
                textValue.value = it
                viewModel.setDesc(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = textFieldOutline,
                focusedLabelColor = textFieldOutline,
                cursorColor = Primary,
                containerColor = textFieldBG,
                unfocusedBorderColor = textFieldOutline,
//                placeholderColor = Color.LightGray,
//                textColor = darkBG
            ),
            keyboardOptions = KeyboardOptions.Default,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
        )
    }

}

@Composable
fun privacyButton(status: Boolean, thisOnClick: () -> Unit) {

    val icon = if (status) Icons.Filled.Lock else Icons.Filled.LockOpen
    val iconColor = if (status) offWhiteBack else Color.Gray

    Spacer(modifier = Modifier.padding(start = 25.dp))

    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable(onClick = thisOnClick)
            .background(color = myGradientGrey)
            .border(
                width = 1.dp,
                color = offWhiteBack,
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = "Toggle Icon", tint = iconColor, modifier = Modifier.size(30.dp))
    }
}

@Composable
fun groupOptionsPhoto(
    selectedImageUri: MutableState<Uri?>,
    thisOnClick: () -> Unit = {}
) {
    Box(modifier = Modifier
        .size(80.dp)
        .graphicsLayer(shadowElevation = 16f, shape = CircleShape)
    ) {

        if (selectedImageUri.value == null){
            Image(
                painter = painterResource(id = R.drawable.noimage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
            )
        } else {
            AsyncImage(model = selectedImageUri.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
            )
        }


        Box(modifier = Modifier
            .fillMaxSize(0.25f)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .border(width = 1.dp, color = Primary, shape = CircleShape)
            .align(Alignment.BottomEnd)
            .padding(4.dp)
            .clickable(onClick = thisOnClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                // Change from email to new pencil
                // old one corrupted and broke the whole app
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Pencil",
            )

        }
    }
}