package com.example.checkmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.checkmate.model.ToDoItem
import com.example.checkmate.ui.PriorityColorProvider
import androidx.compose.foundation.shape.CircleShape

@Composable
fun TodoItemRow(
    todo: ToDoItem,
    isEditing: Boolean,
    editedText: String,
    onCheckedChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditTextChange: (String) -> Unit,
    onEditSave: () -> Unit
) {
    if (isEditing) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = editedText,
                onValueChange = onEditTextChange,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onEditSave) {
                Text("저장")
            }
        }
    } else {
        val priorityColor = PriorityColorProvider.colorFor(todo.priorityEnum())

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = onCheckedChange
            )

            // 우선순위 색상 점 표시
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(priorityColor)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = todo.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = if (todo.completed)
                    MaterialTheme.colorScheme.outline
                else
                    MaterialTheme.colorScheme.onSurface
            )

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "수정")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "삭제")
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))
}
