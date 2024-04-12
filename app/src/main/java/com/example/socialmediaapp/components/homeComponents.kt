import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.components.myCustomFont

@Composable
fun fypLikeButton(likes: Int, value: Boolean, thisOnClick: () -> Unit) {
    Column(modifier = Modifier
        .clickable(
            onClick = thisOnClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (value) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Liked",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Not liked",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = formatLikes(likes),
            modifier = Modifier.padding(start = 4.dp),
            color = Color.White,
            style = TextStyle(
                fontFamily = myCustomFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        )
    }
}

fun formatLikes(likes: Int): String {
    return when {
        likes < 1000 -> likes.toString()
        likes < 1000000 -> "${(likes / 1000.0)}k"
        else -> "${(likes / 1000000.0)}m"
    }
}