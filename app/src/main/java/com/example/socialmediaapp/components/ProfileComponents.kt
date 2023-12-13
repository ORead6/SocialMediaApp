package com.example.socialmediaapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.signIn.UserData


@Composable
fun pfpCircle(
    userData: UserData?
) {
    if (userData?.profilePictureUrl != null && userData.profilePictureUrl != "") {
        AsyncImage(
            model = userData?.profilePictureUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.userpfp),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun GroupsCounter(
    number: Int = 12,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Groups",
            fontFamily = myCustomFont,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = number.toString(),
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun FollowerCounter(
    number: Int = 473,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Followers",
            fontFamily = myCustomFont,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = number.toString(),
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun FollowingCounter(
    number: Int = 274,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Following",
            fontFamily = myCustomFont,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                color = Color.White
            )
        )
        Text(modifier = Modifier
            .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = number.toString(),
            fontFamily = myCustomFont,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun bioSection(
    thisBio: String? = null,
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        if (thisBio != null) {
            Text(
                textAlign = TextAlign.Start,
                text = thisBio,
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun EditProfileButton(
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
            colors = ButtonDefaults.buttonColors(LoginScreensColor),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Edit profile",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = myCustomFont)
            }
        }
    }
}

@Composable
fun postDivider() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
          imageVector = Icons.Filled.GridOn,
            contentDescription = "myPosts",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.padding(5.dp))

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            thickness = 1.dp
        )

    }

}

@Composable
fun userNameDisplay(username: String? = "Test") {
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (username != null) {
            Text(
                textAlign = TextAlign.Start,
                text = username,
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            Text(
                textAlign = TextAlign.Start,
                text = "username",
                fontFamily = myCustomFont,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

