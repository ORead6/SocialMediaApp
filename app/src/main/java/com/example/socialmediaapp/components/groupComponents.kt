package com.example.socialmediaapp.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databaseCalls.databaseCalls
import com.example.socialmediaapp.viewModels.groupViewModel

@Composable
fun groupGrid(
    userGroups: Map<String, Map<String, String>>,
    dbCalls: databaseCalls,
    thisOnClick: (Any?) -> Unit,
) {
    val spacing = 25.dp

    val entries = userGroups.entries.toList()

    LazyColumn {
        itemsIndexed(entries) { index, entry ->
            val groupId = entry.key
            val groupData = entry.value

            Box(
                modifier = Modifier
                    .padding(top = spacing / 2)
            ) {
                // Use groupId and groupData as needed in groupGridItem
                groupGridItem(groupId, groupData, thisOnClick, dbCalls)
            }
        }
    }
}

@Composable
fun groupGridItem(
    id: String,
    groupData: Map<String, String>,
    thisOnClick: (Any?) -> Unit,
    dbCalls: databaseCalls
) {
    if (id != "") {

        var groupPhotoUri = remember {
            mutableStateOf<Uri?>(null)
        }

        LaunchedEffect(true) {
            dbCalls.getGroupPhoto(id) {theUri ->
                if (theUri != null) {
                    groupPhotoUri.value = theUri
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clickable {
                    thisOnClick(id)
                }
            ,
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            ),
            colors = CardDefaults.cardColors(
                //containerColor = textFieldBG
                containerColor = Color(0xff424242)
            ),
        )
        {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 0.dp, start = 4.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    groupData["groupName"]?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Normal,
                                fontFamily = myCustomFont,
                                color = Color.White
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {

                    if (groupPhotoUri == null) {

                    } else {
                        AsyncImage(
                            model = groupPhotoUri.value,
                            contentDescription = "Group Photo",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RectangleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column (modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 4.dp))
            {
                groupData["groupBio"]?.let {
                    Text(text = it,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Normal,
                            fontFamily = myCustomFont,
                            color = Color.White
                        )
                    )
                }
            }



        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(myViewModel: groupViewModel) {

    val textvalue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = textvalue.value,
        onValueChange = {
            textvalue.value = it
            myViewModel.setSearchInv(it)
            },
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(50.dp),
        singleLine = true,
        placeholder = { Text(text = "Enter Group Join Code...") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
//            placeholderColor = Color.LightGray,
//            textColor = darkBG
        )
    )
}

@Composable
fun addGroup(
    thisOnClick: () -> Unit = {},
)  {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = (12.5f).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "google",
                    modifier = Modifier
                        .size(24.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                )

                Text(text = "Create Group",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = myCustomFont)
            }


        }
    }
}

@Composable
fun joinButton(
    thisOnClick: () -> Unit = {},
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = thisOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(buttonGrey),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Gray)
        )
        {
            Text(text = "Join group",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = myCustomFont)
        }


        }
    }