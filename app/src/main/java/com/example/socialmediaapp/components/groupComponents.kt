package com.example.socialmediaapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun groupGrid(userGroups: Map<String, Map<String, String>>) {
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
                groupGridItem(groupId, groupData)
            }
        }
    }
}

@Composable
fun HorizontalLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Adjust vertical padding as needed
    ) {
        Divider(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.95f)
                .height(1.dp)
                .background(Color.Gray) // You can customize the color
        )
    }
}

@Composable
fun groupGridItem(item: String, groupData: Map<String, String>) {
    if (item != "") {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
            ,
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = textFieldBG
            ),
        )
        {
            Column  (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                groupData["name"]?.let {
                    Text(text = it,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            fontFamily = myCustomFont
                        )
                    )
                }

                HorizontalLine()

                groupData["bio"]?.let {
                    Text(text = it,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            fontFamily = myCustomFont
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchBar(
) {

    val textvalue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = textvalue.value,
        onValueChange = { textvalue.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        singleLine = true,
        placeholder = { Text(text = "Search Your Groups...") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = textFieldBG,
            unfocusedBorderColor = textFieldOutline,
            placeholderColor = Color.LightGray,
            textColor = darkBG
        )
    )
}