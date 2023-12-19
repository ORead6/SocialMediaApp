package com.example.socialmediaapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.signIn.UserData
import okhttp3.internal.wait
import kotlin.random.Random

@Composable
fun groupGrid(theUser: UserData?) {
    val userGroups = remember { theUser?.userGroups }
    val listCount = remember {
        (1..(userGroups?.size ?: 0)).toList()
    }

    val spacing = 25.dp

    LazyColumn {
        itemsIndexed(listCount) { index, item ->
            Box(
                modifier = Modifier
                    .padding(top = spacing / 2)
            ) {
                groupGridItem(userGroups?.get(index) ?: "")
            }
        }

    }
}

@Composable
fun groupGridItem(item: String) {
    if (item != "") {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = textFieldBG
            ),
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item)
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