package org.adamdawi.f1journal.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CustomDialog(title: String, show: Boolean, message: String, onDismiss: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            },
            title = { Text(title) },
            text = { Text(message) }
        )
    }
}