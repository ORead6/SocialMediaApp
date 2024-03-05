package com.example.socialmediaapp.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewModels.createGroupViewModel

@Composable
fun header() {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = "NEW GROUP",
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        )
    }
}

@Composable
fun editGroupPhoto(
    selectedImageUri: MutableState<Uri?>,
    thisOnClick: () -> Unit = {}
) {
    Box(modifier = Modifier
        .size(80.dp)
        .graphicsLayer(shadowElevation = 16f, shape = RectangleShape)
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.userpfp),
//            contentDescription = "Profile Picture",
//            modifier = Modifier
//                .size(80.dp)
//                .clip(RectangleShape)
//                .border(width = 1.dp, color = Color.White, shape = RectangleShape),
//            contentScale = ContentScale.Crop
//        )

        AsyncImage(model = selectedImageUri.value, contentDescription = null, contentScale = ContentScale.Crop)

        Box(modifier = Modifier
            .fillMaxSize(0.25f)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .border(width = 1.dp, color = LoginScreensColor, shape = CircleShape)
            .align(Alignment.BottomEnd)
            .padding(4.dp)
            .clickable(onClick = thisOnClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                // Change from email to new pencil
                // old one corrupted and broke the whole app
                painter = painterResource(id = R.drawable.email),
                contentDescription = "Pencil",
            )

        }
    }
}

// Need to pass in viewModel as Parameter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun newGroupName(viewModel: createGroupViewModel) {
    val textValue = remember {
        mutableStateOf("")
    }


    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp)
            .padding(start = 8.dp),
        value = textValue.value,
        onValueChange = {
            textValue.value = it.replace("\n", "")
            viewModel.setGroupName(it)
        },
        label = {
            Text(text = "Group Name", fontFamily = myCustomFont)
            },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = myGradientGrey,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
    )
}

@Composable
fun groupPrivacyButton(viewModel: createGroupViewModel) {
    var isActive by remember { mutableStateOf(false) }

    val icon = if (isActive) Icons.Filled.Lock else Icons.Filled.LockOpen
    val iconColor = if (isActive) Color.Black else myGradientGrey

    Spacer(modifier = Modifier.padding(start = 25.dp))

    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable {
                isActive = !isActive
                viewModel.setPrivacyState(isActive)
            }
            .background(color = offWhiteBack)
            .border(
                width = 1.dp,
                color = myGradientGrey,
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = "Toggle Icon", tint = iconColor, modifier = Modifier.size(30.dp))
    }
}

@Composable
fun privacyText() {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = "Privacy Status",
            fontFamily = myCustomFont,
            style = TextStyle(
                color = myGradientGrey,
                fontWeight = FontWeight.Normal,
                fontSize = 25.sp
            )
        )
    }
}

@Composable
fun createGroupButton(
    thisOnClick: () -> Unit = {},
)  {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(offWhiteBack),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, myGradientGrey)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
                contentAlignment = Alignment.Center
            ) {
                    Text(text = "CREATE GROUP",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = myGradientGrey,
                    fontFamily = myCustomFont)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun newGroupBio(viewModel: createGroupViewModel) {
    val textValue = remember {
        mutableStateOf("")
    }


    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp)
            .padding(start = 8.dp),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            viewModel.setBio(it)
        },
        label = {
            Text(text = "Group Bio", fontFamily = myCustomFont)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = myGradientGrey,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
    )
}




