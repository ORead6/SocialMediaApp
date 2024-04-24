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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.components.ClickableTextElement
import com.example.socialmediaapp.components.HeadingTextComponent
import com.example.socialmediaapp.components.LoginEmailTextField
import com.example.socialmediaapp.components.LoginPasswordTextField
import com.example.socialmediaapp.components.NormalTextComponent
import com.example.socialmediaapp.components.Primary
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.darkBG
import com.example.socialmediaapp.components.loginButtonComponent
import com.example.socialmediaapp.components.myDarkGrey
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.viewModels.LoginViewModel

@Composable
fun LoginScreen(navController: NavController){

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary)
        ) {

            val myLoginViewModel = LoginViewModel()

            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 10.dp)) {
                backButton(thisOnClick = {
                    navController.navigate("loginSelection")
                })
                Spacer(modifier = Modifier.padding(10.dp))
                HeadingTextComponent(value = "Sign in", thisColor = Color.White, alignment = TextAlign.Left)
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

                val context = LocalContext.current

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)
                    .background(Color.White)) {

                    HeadingTextComponent(value = "Welcome Back", thisColor = darkBG, alignment = TextAlign.Left)
                    Spacer(modifier = Modifier.padding(2.dp))
                    NormalTextComponent(
                        value = "Hello there, sign in to continue!",
                        thisColor = Color.Gray,
                        alignment = TextAlign.Left,
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    NormalTextComponent(
                        value = "Email",
                        thisColor = Color.LightGray,
                        alignment = TextAlign.Left,
                        bold = true,
                    )
                    LoginEmailTextField(
                        labelValue = "Enter your email",
                        painterResource = painterResource(id = R.drawable.user),
                        viewModel = myLoginViewModel
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(
                        value = "Password",
                        thisColor = Color.LightGray,
                        alignment = TextAlign.Left,
                        bold = true,
                    )
                    LoginPasswordTextField(
                        labelValue = "Enter your password", painterResource = painterResource(
                            id = R.drawable.lock
                        ),
                        viewModel = myLoginViewModel
                    )

                    Spacer(modifier = Modifier.padding(8.dp))
                    ClickableTextElement(
                        nonClickColor = Color.White,
                        clickColor = Primary,
                        fullText = "Forgot Password?",
                        clickableText = "Forgot Password?",
                        alignment = Alignment.Start,
                        onClick = {
                            myLoginViewModel.forgotPassword(myLoginViewModel.emailVal.value, context)
                        }
                    )

                    Spacer(modifier = Modifier.padding(12.dp))

                    val thisContext = LocalContext.current

                    loginButtonComponent(value = "Sign in",
                    thisOnClick = {
                        myLoginViewModel.login(myLoginViewModel.emailVal.value, myLoginViewModel.passVal.value, navController, thisContext)
                    })

                    Spacer(modifier = Modifier.weight(1f))
                    ClickableTextElement(
                        nonClickColor = Color.Gray,
                        clickColor = Primary,
                        fullText = "Don't have an account? Sign up",
                        clickableText = "Sign up",
                        onClick = {
                            navController.navigate("register")
                        }
                    )

                }
            }
        }

    }

}

//@Preview
//@Composable
//fun DefaultPreviewOfSignUpScreen() {
//    LoginScreen()
//}

