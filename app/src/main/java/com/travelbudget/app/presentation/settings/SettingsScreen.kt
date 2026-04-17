package com.travelbudget.app.presentation.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelbudget.app.ui.theme.TravelBudgetTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onNavigateToAuth: () -> Unit = {}
) {

    var showClearDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    SettingsContent(
        onClearCacheClick = {
            showClearDialog = true
        },
        onLogoutClick = {
            showLogoutDialog = true
        }
    )

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear Local Cache") },
            text = { Text("Are you sure you want to clear cached expense data?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearLocalCache {
                            showClearDialog = false
                        }
                    }
                ) { Text("Clear") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDialog = false }
                ) { Text("Cancel") }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout {
                            showLogoutDialog = false
                            onNavigateToAuth()
                        }
                    }
                ) { Text("Logout") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) { Text("Cancel") }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {

    TravelBudgetTheme {
        SettingsContent(
            onClearCacheClick = {},
            onLogoutClick = {}
        )
    }
}