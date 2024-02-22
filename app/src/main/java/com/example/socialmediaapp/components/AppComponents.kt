@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.socialmediaapp.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewModels.LoginViewModel
import com.example.socialmediaapp.viewModels.registerViewModel

val myCustomFont = FontFamily(
    Font(R.font.montserratbold, weight = FontWeight.Bold),
    Font(R.font.montserratreg, weight = FontWeight.Normal)
)

@Composable
fun NormalTextComponent(
    value: String,
    thisColor: Color,
    alignment: TextAlign = TextAlign.Center,
    bold: Boolean = false,
) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 20.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color = thisColor,
            fontFamily = myCustomFont
        ),
        textAlign = alignment,
    )
}

@Composable
fun HeadingTextComponent(value: String, thisColor: Color, alignment: TextAlign) {

    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontFamily = myCustomFont,
            color = thisColor
        ),
        textAlign = alignment
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextField(labelValue: String, painterResource: Painter, viewModel: registerViewModel) {

    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            viewModel.setEmailVal(it)
        },
        label = { Text(labelValue, fontFamily = myCustomFont) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
            )
        },
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameTextField(labelValue: String, painterResource: Painter, viewModel: registerViewModel) {

    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            viewModel.setUsernameVal(it)
        },
        label = { Text(labelValue, fontFamily = myCustomFont) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
            )
        },
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    labelValue: String,
    painterResource: Painter,
    seePass: Boolean = true,
    viewModel: registerViewModel,
    passwordType: Int
) {

    val password = remember {
        mutableStateOf("")
    }

    val passwordVisibility = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        value = password.value,
        onValueChange = {
            password.value = it
            if (passwordType == 1) {
                viewModel.setPass1Val(it)
            } else {
                viewModel.setPass2Val(it)
            }
            },
        label = { Text(labelValue, fontFamily = myCustomFont) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
            )
        },
        trailingIcon = {
            if (seePass) {
                val iconImg = if (passwordVisibility.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                var description = if (passwordVisibility.value) {
                    "Hide Password"
                } else {
                    "Show Password"
                }

                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(imageVector = iconImg, contentDescription = description)
                }
            }
        },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation('*'),
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp, letterSpacing = 5.sp)
    )
}

@Composable
fun loginButtonComponent(value: String, thisOnClick: () -> Unit) {
    Button(
        onClick = thisOnClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(LoginScreensColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = myCustomFont)
        }
    }
}

@Composable
fun dividerTextComponent(value: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )

        Text(modifier = Modifier.padding(8.dp),
            text = value,
            fontSize = 18.sp,
            color = TextColor,
            fontFamily = myCustomFont)

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = GrayColor,
            thickness = 1.dp
        )
    }

}

@Composable
fun ClickableTextElement(
    nonClickColor: Color,
    clickColor: Color,
    fullText: String,
    clickableText: String,
    onClick: () -> Unit,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    val gradBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xff613bf7),
            Color(0xff9178f5)
        )
    )

    val clickText = remember(fullText, clickableText) {
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 14.sp, color = nonClickColor, fontFamily = myCustomFont)) {
                append(fullText.substring(0, fullText.indexOf(clickableText)))
            }
            withStyle(style = SpanStyle(fontSize = 14.sp, brush = gradBrush, fontWeight = FontWeight.Bold, fontFamily = myCustomFont)) {
                append(clickableText)
                addStringAnnotation(
                    tag = "Clickable",
                    annotation = clickableText,
                    start = length - clickableText.length,
                    end = length
                )
            }
            withStyle(style = SpanStyle(fontSize = 18.sp, color = Primary, fontFamily = myCustomFont)) {
                append(fullText.substring(fullText.indexOf(clickableText) + clickableText.length))
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, end = 16.dp),
        horizontalAlignment = alignment
    ) {
        ClickableText(text = clickText, onClick = { offset ->
            clickText.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    onClick()
                }
        })
    }
}

@Composable
fun GoogleLoginButtonComponent(label: String, onSignInClick: () -> Unit) {
    Button(
        onClick = onSignInClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(15.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "google",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LoginScreensColor,
                textAlign = TextAlign.Center,
                fontFamily = myCustomFont
            )
        }
    }
}

@Composable
fun RegisterButtonComponent(label: String, registerOnClick: () -> Unit){
    Button(
        onClick = registerOnClick,
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
            Text(text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = myCustomFont)
        }
    }
}

@Composable
fun boxBackground(colorOfBox: Color, heightOfBox: Dp) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(heightOfBox)
        .background(color = colorOfBox)
    ) {

    }

}

@Composable
fun backButton(thisOnClick: () -> Unit) {
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
fun uploadPfpButton(label: String) {
    Box(modifier = Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { /* TODO: Handle button click */ },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(40.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(textFieldBG),
            shape = RoundedCornerShape(15.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = "upload",
                    modifier = Modifier
                        .size(24.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkBG,
                    textAlign = TextAlign.Center,
                    fontFamily = myCustomFont
                )
            }
        }
    }
}

@Composable
fun pfpIcon() {
    Box(modifier = Modifier
        .then(Modifier.sizeIn(maxWidth = 200.dp)),
        contentAlignment = Alignment.CenterStart) {
        Image(
            painter = painterResource(id = R.drawable.userpfp),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    shape = RoundedCornerShape(50.dp)
                    clip = true
                    shadowElevation = 12f
                },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioInputWithCharacterLimit(
    maxCharacterLimit: Int,
    labelValue: String = "Enter your Bio"
) {
    val textValue = remember {
        mutableStateOf("")
    }

    Column  {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(20.dp),
            value = textValue.value,

            onValueChange = {
                if (it.length <= maxCharacterLimit)
                    textValue.value = it
            },

            label = { Text(labelValue, fontFamily = myCustomFont) },

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = textFieldOutline,
                focusedLabelColor = textFieldOutline,
                cursorColor = Primary,
                containerColor = textFieldBG,
                unfocusedBorderColor = textFieldOutline,
                placeholderColor = Color.LightGray,
                textColor = darkBG
            ),

            singleLine = false,
            keyboardOptions = KeyboardOptions.Default,
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
        )

        Text(
            text = "Characters used: ${textValue.toString().length  - 30}/$maxCharacterLimit",
            color = if (textValue.toString().length > maxCharacterLimit) Color.Red else Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier.padding(end = 16.dp, bottom = 8.dp, start = 16.dp)
        )
    }
}

@Composable
fun smallTextComponent(value: String, thisColor: Color, alignment: TextAlign = TextAlign.Center, bold: Boolean = false) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 20.dp),
        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color = thisColor,
            fontFamily = myCustomFont
        ),
        textAlign = alignment,

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPasswordTextField(
    labelValue: String,
    painterResource: Painter,
    seePass: Boolean = true,
    viewModel: LoginViewModel,
) {

    val password = remember {
        mutableStateOf("")
    }

    val passwordVisibility = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        value = password.value,
        onValueChange = {
            password.value = it
            viewModel.setPassVal(it)

        },
        label = { Text(labelValue, fontFamily = myCustomFont) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
            )
        },
        trailingIcon = {
            if (seePass) {
                val iconImg = if (passwordVisibility.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                var description = if (passwordVisibility.value) {
                    "Hide Password"
                } else {
                    "Show Password"
                }

                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(imageVector = iconImg, contentDescription = description)
                }
            }
        },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation('*'),
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp, letterSpacing = 5.sp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailTextField(labelValue: String, painterResource: Painter, viewModel: LoginViewModel) {

    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            viewModel.setEmailVal(it)
        },
        label = { Text(labelValue, fontFamily = myCustomFont) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxHeight()
            )
        },
        textStyle = TextStyle(fontSize = 18.sp, lineHeight = 20.sp)
    )
}



