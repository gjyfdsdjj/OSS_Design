package com.example.checkmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.checkmate.model.Priority

@Composable
fun AddTodoSection(
    newTodoText: String,
    onTextChange: (String) -> Unit,
    selectedPriority: Priority,
    onPriorityChange: (Priority) -> Unit,
    onAddClick: () -> Unit
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = onTextChange,
                label = { Text("할 일 입력") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onAddClick) {
                Text("추가")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 우선순위 선택 드롭다운
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedPriority.label,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("우선순위") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Priority.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label) },
                        onClick = {
                            onPriorityChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
