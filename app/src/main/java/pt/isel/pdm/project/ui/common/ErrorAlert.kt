package pt.isel.pdm.project.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.project.ui.common.theme.GomokuTheme

@Composable
fun Error(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            OutlinedButton(
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = onDismiss
            ) {
                Text(text = buttonText)
            }
        },
        title = { Text(text = title) },
        text = { Text(text = message) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Warning"
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ErrorAlertImplPreview() {
    GomokuTheme {
        Error(
            title = "Error accessing server",
            message = "Could not ...",
            buttonText = "OK",
            onDismiss = { }
        )
    }
}