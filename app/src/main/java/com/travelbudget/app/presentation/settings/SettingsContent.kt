package com.travelbudget.app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.travelbudget.app.ui.components.AppTopBar

@Composable
fun SettingsContent(
    onClearCacheClick: () -> Unit
) {

    Scaffold(
        topBar = {
            AppTopBar(
                showShare = false
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedButton(
                onClick = onClearCacheClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Local Cache")
            }
        }
    }
}