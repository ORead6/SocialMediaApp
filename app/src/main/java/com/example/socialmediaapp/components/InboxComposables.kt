import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { expanded = false }),
        )

        Log.d("FILTERED_OPTIONS", "Filtering options for text: $textFieldValue")
        val filteredOptions = options.filter { it.contains(textFieldValue, ignoreCase = true) }
        Log.d("FILTERED_OPTIONS", "Filtered options: $filteredOptions")

        DropdownMenu(
            expanded = textFieldValue.isNotBlank(),
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(onClick = {
                    textFieldValue = option
                    onValueChange(option)
                    expanded = false
                }, text = {
                    Text(text = option)
                })
            }
        }
    }
}
