package com.example.socialmediaapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewModels.uploadViewModel


@Composable
fun mediaPicker(thisOnClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.selectmedia),
        contentDescription = "Media Selector",
        modifier = Modifier
            .size(80.dp)
            .clip(RectangleShape)
            .border(width = 1.dp, color = Color.White, shape = RectangleShape)
            .clickable(onClick = thisOnClick))
}

@Composable
fun uploadHeader() {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = "NEW UPLOAD",
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mediaDescription(myViewModel: uploadViewModel) {
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
            myViewModel.setCaption(it)
        },
        label = {
            Text(text = "Caption...", fontFamily = myCustomFont)
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



