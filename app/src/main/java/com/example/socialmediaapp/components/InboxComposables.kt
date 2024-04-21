import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.socialmediaapp.components.Primary
import com.example.socialmediaapp.components.buttonGrey
import com.example.socialmediaapp.components.messageBlue
import com.example.socialmediaapp.components.myCustomFont
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.components.textFieldBG
import com.example.socialmediaapp.components.textFieldOutline
import com.example.socialmediaapp.messaging.messagingDataStruc
import com.example.socialmediaapp.viewModels.directMessageViewModel
import com.example.socialmediaapp.viewModels.inboxViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

@OptIn(UnstableApi::class)
@Composable
fun messageCardGrid(messageList: MutableState<List<messagingDataStruc>>, userMessageID: String) {

    Log.d("MESSAGES", messageList.toString())

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(messageList.value.size) {
        lazyGridState.scrollToItem(messageList.value.size - 1)
    }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        state = lazyGridState
    ) {
        items(messageList.value) { post ->
            val received = (post.senderID == userMessageID)
            val message = post.msg
            val timestamp = post.timestamp

            messageItem(message = message, received = received, timestamp = timestamp)

        }
    }


}

@SuppressLint("SimpleDateFormat")
@Composable
fun messageItem(message: String, received: Boolean, timestamp: Timestamp) {

    val date = timestamp.toDate()
    val sdf = SimpleDateFormat("HH:mm dd-MM")
    val theTime = sdf.format(date)

    val bgColor = if (received) {
        offWhiteBack
    } else {
        messageBlue
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (received) Alignment.Start else Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor, shape = RoundedCornerShape(5.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = message,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = myCustomFont,
                            color = Color.Black
                        )
                    )

                    Spacer(Modifier.padding(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = theTime,
                            style = TextStyle(
                                fontFamily = myCustomFont,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun messageField(textvalue: MutableState<String>, myViewModel: directMessageViewModel) {

    OutlinedTextField(
        value = textvalue.value,
        onValueChange = {
            textvalue.value = it
            myViewModel.setMsg(it)
        },
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(50.dp),
        singleLine = true,
        placeholder = { Text(text = "Message...") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textFieldOutline,
            focusedLabelColor = textFieldOutline,
            cursorColor = Primary,
            containerColor = Color.White,
            unfocusedBorderColor = textFieldOutline,
//            placeholderColor = Color.LightGray,
//            textColor = darkBG
        )
    )
}

@Composable
fun sendMsgButton(thisOnClick: () -> Unit) {
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
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Gray)
        )
        {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "User Icon",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }


    }
}


