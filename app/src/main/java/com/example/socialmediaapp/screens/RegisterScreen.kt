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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.components.ClickableTextElement
import com.example.socialmediaapp.components.EmailTextField
import com.example.socialmediaapp.components.HeadingTextComponent
import com.example.socialmediaapp.components.NormalTextComponent
import com.example.socialmediaapp.components.PasswordTextField
import com.example.socialmediaapp.components.Primary
import com.example.socialmediaapp.components.UsernameTextField
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.darkBG
import com.example.socialmediaapp.components.loginButtonComponent
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.viewModels.registerViewModel

@Composable
fun RegisterScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary)
        ){
            Column( modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 10.dp)) {
                backButton(thisOnClick = {
                    navController.navigate("loginSelection")
                })
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

                val theContext = LocalContext.current

                val myRegisterViewModel = registerViewModel(theContext)

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)
                    .background(Color.White)) {

                    HeadingTextComponent(value = "Hey There", thisColor = darkBG, alignment = TextAlign.Left)
                    Spacer(modifier = Modifier.padding(2.dp))
                    NormalTextComponent(
                        value = "Fill this in to continue!",
                        thisColor = Color.Gray,
                        alignment = TextAlign.Left,
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    NormalTextComponent(value = "Enter your email", thisColor = Color.LightGray, alignment = TextAlign.Left, bold = true)
                    EmailTextField(labelValue = "Email", painterResource = painterResource(id = R.drawable.email), viewModel = myRegisterViewModel)
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(
                        value = "Create your username",
                        thisColor = Color.LightGray,
                        alignment = TextAlign.Left,
                        bold = true,
                    )
                    UsernameTextField(
                        labelValue = "Username",
                        painterResource = painterResource(id = R.drawable.user),
                        viewModel = myRegisterViewModel
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(
                        value = "Choose a password",
                        thisColor = Color.LightGray,
                        alignment = TextAlign.Left,
                        bold = true,
                    )
                    PasswordTextField(labelValue = "Password", painterResource = painterResource(id = R.drawable.lock), seePass = false, viewModel = myRegisterViewModel, passwordType  = 1)
                    Spacer(modifier = Modifier.padding(8.dp))
                    NormalTextComponent(
                        value = "Confirm Your Password",
                        thisColor = Color.LightGray,
                        alignment = TextAlign.Left,
                        bold = true,
                    )
                    PasswordTextField(
                        labelValue = "Password",
                        painterResource = painterResource(id = R.drawable.lock),
                        seePass = false,
                        viewModel = myRegisterViewModel,
                        passwordType = 2
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    loginButtonComponent(value = "Create Account", thisOnClick = {
                        myRegisterViewModel.registerUser(myRegisterViewModel.emailVal.value, myRegisterViewModel.usernameVal.value, myRegisterViewModel.pass1Val.value, myRegisterViewModel.pass2Val.value, navController)
                    })

                    Spacer(modifier = Modifier.weight(1f))
                    ClickableTextElement(
                        nonClickColor = Color.Gray,
                        clickColor = Primary,
                        fullText = "Already have an account? Sign in",
                        clickableText = "Sign in",
                        onClick = {
                            navController.navigate("login")
                        }
                    )

                }
            }
        }

    }
}


//@Preview
//@Composable
//fun DefaultPreviewOfRegisterScreen() {
//    RegisterScreen()
//}