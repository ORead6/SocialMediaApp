package com.example.socialmediaapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.signIn.UserData
import com.example.socialmediaapp.viewModels.editprofileViewModel
import com.example.socialmediaapp.viewModels.registerViewModel

@Composable
fun Header(value: String, thisColor: Color = Color.White) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            color = thisColor,
            fontFamily = myCustomFont,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
fun editPfpCircle(
    userData: UserData?
) {
    Box(modifier = Modifier
        .size(80.dp)
        .graphicsLayer(shadowElevation = 16f, shape = CircleShape)
    ) {
        if (userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.White, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.userpfp),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.White, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(modifier = Modifier
            .fillMaxSize(0.25f)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .border(width = 1.dp, color = LoginScreensColor, shape = CircleShape)
            .align(Alignment.BottomEnd)
            .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.pencil),
                contentDescription = "Pencil",
            )
        }
    }
}

val myViewModel = editprofileViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textField(labelValue: String, viewModel: editprofileViewModel = myViewModel) {

    val textValue = remember {
        mutableStateOf(labelValue)
    }

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(20.dp),
            value = textValue.value,
            onValueChange = {
                textValue.value = it
                viewModel.setUsername(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = textFieldOutline,
                focusedLabelColor = textFieldOutline,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bioField(labelValue: String, viewModel: editprofileViewModel = myViewModel) {

    val textValue = remember {
        mutableStateOf(labelValue)
    }

    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(100.dp),
            value = textValue.value,
            onValueChange = {
                textValue.value = it
                viewModel.setUsername(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = textFieldOutline,
                focusedLabelColor = textFieldOutline,
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

}

@Preview
@Composable
fun defPrev() {

}