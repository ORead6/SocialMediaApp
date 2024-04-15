package com.example.socialmediaapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.example.socialmediaapp.R

@Composable
fun followUserButton(
    followStatus: Boolean,
    thisOnClick: () -> Unit = {},
)  {

    val buttonColor = if (followStatus) subtleGreen else buttonGrey

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
            colors = ButtonDefaults.buttonColors(buttonColor),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
                contentAlignment = Alignment.Center
            ) {

                if(followStatus) {
                    Image(
                        painter = painterResource(id = R.drawable.followingdumbell),
                        contentDescription = "followingDumbell",
                        modifier = Modifier
                            .size(45.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp),
                        colorFilter = ColorFilter.tint(color = Color.White)
                    )

                    Text(
                        text = "Following",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = myCustomFont
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.dumbell),
                        contentDescription = "dumbell",
                        modifier = Modifier
                            .size(35.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp),
                        colorFilter = ColorFilter.tint(color = Color.White)
                    )

                    Text(
                        text = "Follow",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = myCustomFont
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun previewThis() {
    followUserButton(followStatus = true) {

    }
}