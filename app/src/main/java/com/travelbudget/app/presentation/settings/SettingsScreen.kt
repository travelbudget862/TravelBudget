package com.travelbudget.app.presentation.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelbudget.app.ui.theme.TravelBudgetTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onCacheCleared: () -> Unit = {}
) {

    var showDialog by remember { mutableStateOf(false) }

    SettingsContent(
        onClearCacheClick = {
            showDialog = true
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Clear Local Cache") },
            text = {
                Text("Are you sure you want to clear cached expense data?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearLocalCache {
                            showDialog = false
                            onCacheCleared()
                        }
                    }
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {

    TravelBudgetTheme {
        SettingsContent(
            onClearCacheClick = {}
        )
    }
}