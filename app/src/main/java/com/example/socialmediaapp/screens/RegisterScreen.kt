package com.example.socialmediaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.R
import com.example.socialmediaapp.components.ClickableTextElement
import com.example.socialmediaapp.components.HeadingTextComponent
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.NormalTextField
import com.example.socialmediaapp.components.NormalTextComponent
import com.example.socialmediaapp.components.PasswordTextField
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.darkBG
import com.example.socialmediaapp.components.loginButtonComponent

@Composable
fun RegisterScreen () {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreensColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LoginScreensColor)
        ){
            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 10.dp)) {
                backButton(thisOnClick = {})
                Spacer(modifier = Modifier.padding(10.dp))
                HeadingTextComponent(value = "Create an Account", thisColor = Color.White, alignment = TextAlign.Left)
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

                    HeadingTextComponent(value = "Hey There", thisColor = darkBG, alignment = TextAlign.Left)
                    Spacer(modifier = Modifier.padding(2.dp))
                    NormalTextComponent(value = "Fill this in to continue!", thisColor = Color.Gray, alignment = TextAlign.Left)
                    Spacer(modifier = Modifier.padding(12.dp))
                    NormalTextComponent(value = "Enter your email", thisColor = Color.LightGray, alignment = TextAlign.Left, bold = true)
                    NormalTextField(labelValue = "Email", painterResource = painterResource(id = R.drawable.email))
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(value = "Create your username", thisColor = Color.LightGray, alignment = TextAlign.Left, bold = true)
                    NormalTextField(labelValue = "Username", painterResource = painterResource(id = R.drawable.user))
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(value = "Choose a password", thisColor = Color.LightGray, alignment = TextAlign.Left, bold = true)
                    PasswordTextField(labelValue = "Password", painterResource = painterResource(id = R.drawable.lock), seePass = false)
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(value = "Confirm Your Password", thisColor = Color.LightGray, alignment = TextAlign.Left, bold = true)
                    PasswordTextField(labelValue = "Password", painterResource = painterResource(id = R.drawable.lock), seePass = false)
                    Spacer(modifier = Modifier.padding(16.dp))
                    loginButtonComponent(value = "Create Account", thisOnClick = {})

                    Spacer(modifier = Modifier.weight(1f))
                    ClickableTextElement(
                        nonClickColor = Color.Gray,
                        clickColor = LoginScreensColor,
                        fullText = "Already have an account? Sign in",
                        clickableText = "Sign in",
                        onClick = {

                        }
                    )

                }
            }
        }

    }
}


@Preview
@Composable
fun DefaultPreviewOfRegisterScreen() {
    RegisterScreen()
}