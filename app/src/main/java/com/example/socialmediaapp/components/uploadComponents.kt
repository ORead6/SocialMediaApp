package com.example.socialmediaapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
            .clickable(onClick = thisOnClick)
    )
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

@Composable
fun postButton(thisOnClick: () -> Unit) {
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
                Text(text = "Post!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = myGradientGrey,
                    fontFamily = myCustomFont)
            }
        }
    }
}

@Composable
fun progressDisplay(currentProgress: Float, msgVal: String){
    val currProgress = (currentProgress / 100)
    val strokeWidth = 25f // Adjust the width of the border as needed
    val progressColor = Color(0xFF84aff5) // Color representing the progress
    val backgroundColor = Color.LightGray // Color of the background circle

    Canvas(modifier = Modifier.aspectRatio(1f)) {
        val innerRadius = size.minDimension / 2 - strokeWidth / 2
        val startAngle = 270f // Start angle at 12 o'clock position
        val sweepAngle = currProgress * 360 // Convert progress to degrees
        drawArc(
            color = backgroundColor,
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        drawArc(
            color = progressColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawIntoCanvas { canvas ->
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f

            drawContext.canvas.nativeCanvas.drawText(
                msgVal,
                centerX,
                centerY,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 80f // Adjust text size as needed
                }
            )
        }

    }
}



