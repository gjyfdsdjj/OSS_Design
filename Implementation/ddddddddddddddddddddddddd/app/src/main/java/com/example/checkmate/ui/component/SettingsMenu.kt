package com.example.checkmate.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

@Composable
fun SettingsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("다크모드")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = {
                            onDarkModeToggle(it)
                        }
                    )
                }
            },
            onClick = {} // Switch에서 처리하므로 따로 클릭 이벤트 없음
        )
    }
}
