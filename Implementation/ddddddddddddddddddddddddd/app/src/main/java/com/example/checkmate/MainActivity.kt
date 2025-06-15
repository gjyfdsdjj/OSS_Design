package com.example.checkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.checkmate.ui.*
import com.example.checkmate.ui.theme.CheckMateTheme
import com.example.checkmate.ui.group.GroupCreateScreen
import com.example.checkmate.ui.group.GroupListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var isLoggedIn by remember { mutableStateOf(false) }
            var showGroupList by remember { mutableStateOf(false) }
            var showGroupCreate by remember { mutableStateOf(false) }

            CheckMateTheme(darkTheme = isDarkMode) {
                when {
                    !isLoggedIn -> {
                        AuthScreen(onAuthSuccess = { isLoggedIn = true })
                    }
                    showGroupList -> {
                        GroupListScreen(
                            onBack = { showGroupList = false }
                        )
                    }
                    showGroupCreate -> {
                        GroupCreateScreen(
                            onBack = { showGroupCreate = false },
                            onCreated = { showGroupCreate = false }
                        )
                    }
                    else -> {
                        CalendarTodoScreen(
                            onLogout = { isLoggedIn = false },
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { isDarkMode = it },
                            onNavigateToGroupList = { showGroupList = true },
                            onNavigateToGroupCreate = { showGroupCreate = true }
                        )
                    }
                }
            }

        }
    }
}
