package com.example.socialmediaapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.components.Header
import com.example.socialmediaapp.components.LoginScreensColor
import com.example.socialmediaapp.components.ThreeDotsMenu
import com.example.socialmediaapp.components.backButton
import com.example.socialmediaapp.components.bioField
import com.example.socialmediaapp.components.editPfpCircle
import com.example.socialmediaapp.components.myGradientGrey
import com.example.socialmediaapp.components.textField
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.signIn.UserData

@Composable
fun EditProfileScreen (
    userData: UserData?,
    onSignOut: () -> Unit = {},
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradientGrey)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(myGradientGrey)
        ) {

            val dbCalls = databaseCalls("")

            val theContext = LocalContext.current

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, start = 28.dp, end = 28.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    backButton(thisOnClick = {
                        navController.navigate("Profile")
                    })

                    Spacer(modifier = Modifier.weight(1f))

                    ThreeDotsMenu(onMenuItemClick = {
                        if (it == "Logout") {
                            onSignOut()
                        }

                        if (it == "DELETE") {

                            dbCalls.getCurrUser {
                                onSignOut()
                                dbCalls.deleteAccount(it) {
                                    Toast.makeText(theContext, "Account Deleted!", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    })
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Header(value = "Profile")
                }
            }

            Spacer(modifier = Modifier.padding(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    ),
                //contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-40).dp)

                ) {
                    editPfpCircle(userData = null)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                )
                {
                    Spacer(modifier = Modifier.padding(30.dp))
                    textField(labelValue = userData?.username ?: "Username")
                    Spacer(modifier = Modifier.padding(10.dp))
                    bioField(labelValue = userData?.bio.toString())
                }
            }


        }
    }
}

@Preview
@Composable
fun defPrev() {
    EditProfileScreen(navController = rememberNavController(), userData = null)
}