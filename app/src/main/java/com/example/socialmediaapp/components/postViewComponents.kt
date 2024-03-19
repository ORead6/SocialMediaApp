package com.example.socialmediaapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.R

@Composable
fun postBackButton(thisOnClick: () -> Unit) {
    Button(
        onClick = thisOnClick,
        modifier = Modifier
            .fillMaxWidth(0.1f)
            .fillMaxHeight(0.05f),
        contentPadding = PaddingValues(top = 8.dp, end = 8.dp, bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "back",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )

        }
    }
}

@Composable
fun likeButton(likes: Int, value: Boolean, thisOnClick: () -> Unit) {
    Row(modifier = Modifier
        .clickable(
            onClick = thisOnClick
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (value) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Liked",
                tint = Color.Red
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Not liked",
                tint = Color.Black
            )
        }
        Text(
            text = likes.toString(),
            modifier = Modifier.padding(start = 4.dp),
            color = Color.Black
        )
    }
}

@Composable
fun postCaption(caption: String) {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = caption, style = TextStyle(
            fontSize = 14.sp
        ),
        color = Color.White)
    }
}