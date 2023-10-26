package com.example.socialmediaapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.components.ClickableTextElement
import com.example.socialmediaapp.components.GoogleLoginButtonComponent
import com.example.socialmediaapp.components.HeadingTextComponent
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.NormalTextComponent
import com.example.socialmediaapp.components.RegisterButtonComponent

@Composable
fun LoginSelectionScreen(){

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreensColor)
            .padding(28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LoginScreensColor)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            HeadingTextComponent(
                value = "Welcome",
                thisColor = Color.White,
                alignment = TextAlign.Left
            )
            Spacer(modifier = Modifier.padding(20.dp))
            NormalTextComponent(
                value = "Find your motivation",
                thisColor = Color.White,
                alignment = TextAlign.Left,
                bold = false
            )
            Spacer(modifier = Modifier.padding(2.dp))
            NormalTextComponent(
                value = "seamlessly & effortlessly",
                thisColor = Color.White,
                alignment = TextAlign.Left,
                bold = true
            )
            Spacer(modifier = Modifier.padding(25.dp))
            GoogleLoginButtonComponent(label = "Sign in with Google")
            Spacer(modifier = Modifier.padding(6.dp))
            RegisterButtonComponent(label = "Create an account")
            Spacer(modifier = Modifier.padding(8.dp))
            ClickableTextElement(
                nonClickColor = Color.LightGray,
                clickColor = Color.White,
                fullText = "Already have an account? Sign in",
                clickableText = "Sign in",
                onClick = {
                    Log.d("Account", "Already Has Account")
                })
            Spacer(modifier = Modifier.padding(25.dp))
        }
    }

}

@Preview
@Composable
fun DefaultPreviewOfLoginSelectionScreen() {
    LoginSelectionScreen()
}
