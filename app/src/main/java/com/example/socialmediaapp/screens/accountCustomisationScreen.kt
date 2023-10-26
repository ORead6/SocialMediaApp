package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.components.BioInputWithCharacterLimit
import com.example.socialmediaapp.components.HeadingTextComponent
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.pfpIcon
import com.example.socialmediaapp.components.uploadPfpButton

@Composable
fun accountCustomisation() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreensColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LoginScreensColor)
        ) {

            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 10.dp)) {
                backButton(thisOnClick = {})
                Spacer(modifier = Modifier.padding(10.dp))
                HeadingTextComponent(value = "My Account", thisColor = Color.White, alignment = TextAlign.Left)
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)
                    .background(Color.White)) {

                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        pfpIcon()
                        uploadPfpButton(label = "Upload a pfp")
                    }

                    BioInputWithCharacterLimit(maxCharacterLimit = 10)

                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreviewOfAccountCreation() {
    accountCustomisation()
}