package com.travelbudget.app.presentation.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.travelbudget.app.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseContent(
    uiState: AddEditExpenseUiState,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    val categories = listOf("Food", "Hotel", "Transport", "Other")
    var expanded by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                showShare = false,
                onSettingsClick = onSettingsClick
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = uiState.title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                onCategoryChange(category)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.amount,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.date,
                onValueChange = {},
                readOnly = true,
                label = { Text("Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isEditMode) "Update Expense" else "Save Expense")
            }

            if (uiState.isEditMode) {
                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Expense")
                }
            }
        }
        if (showDatePicker) {

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formattedDate = java.text.SimpleDateFormat(
                                    "dd MMM yyyy",
                                    java.util.Locale.getDefault()
                                ).format(java.util.Date(millis))

                                onDateChange(formattedDate)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}