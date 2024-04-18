import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.socialmediaapp.components.myCustomFont
import com.example.socialmediaapp.components.offWhiteBack
import com.example.socialmediaapp.messaging.messagingDataStruc
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

@OptIn(UnstableApi::class)
@Composable
fun messageCardGrid(messageList: MutableState<List<messagingDataStruc>>, userMessageID: String) {

    Log.d("MESSAGES", messageList.toString())

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(0.6f),
        columns = GridCells.Fixed(1)
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

    val sdf = SimpleDateFormat("HH:mm")

   val theTime = sdf.format(date)


    Card (
        modifier = Modifier
            .fillMaxSize()
            .heightIn(40.dp)
            .widthIn(350.dp)
            .background(offWhiteBack, shape = RoundedCornerShape(5.dp))
    ) {
        Column (
            modifier = Modifier.padding(8.dp)
        ){
            Text(
                text = message,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = myCustomFont,
                    color = Color.Black
                )
            )

            Spacer(Modifier.padding(4.dp))

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = theTime,
                    style = TextStyle(
                        fontFamily = myCustomFont,
                        color = Color.Black
                    )
                )
            }

        }

    }


}
