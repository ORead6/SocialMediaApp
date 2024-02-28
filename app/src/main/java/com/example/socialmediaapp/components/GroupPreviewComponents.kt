package com.example.socialmediaapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun groupNameDisplay(groupName: String) {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (groupName != null) {
            Text(
                textAlign = TextAlign.Start,
                text = groupName,
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            )
        } else {
            Text(
                textAlign = TextAlign.Start,
                text = "GROUP",
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            )
        }
    }
}


@Composable
fun overviewButton(modifier: Modifier = Modifier, thisOnClick: () -> Unit) {

    Column (modifier = modifier
        .padding(start = 8.dp, end = 8.dp)) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Overview",
                    style = TextStyle(
                        fontSize = 11.sp ,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}

@Composable
fun leaderboardButton(modifier: Modifier = Modifier, thisOnClick: () -> Unit) {

    Column (modifier = modifier
        .padding(start = 8.dp, end = 8.dp)) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Leaderboard",
                    style = TextStyle(
                        fontSize = 11.sp ,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}

@Composable
fun mediaButton(modifier: Modifier = Modifier, thisOnClick: () -> Unit) {

    Column (modifier = modifier
        .padding(start = 8.dp, end = 8.dp)) {
        Button(
            onClick = thisOnClick ,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Media",
                    style = TextStyle(
                        fontSize = 11.sp ,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}
