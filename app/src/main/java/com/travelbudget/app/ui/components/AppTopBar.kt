package com.travelbudget.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.travelbudget.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    showShare: Boolean = false,
    onShareClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    TopAppBar(
        title = {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_travelbudget_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(28.dp),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "TravelBudget",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        actions = {

            if (showShare) {
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share"
                    )
                }
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    )
}