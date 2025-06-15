package com.example.checkmate.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerContent(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onClose: () -> Unit,
    onNavigateToGroupList: () -> Unit,
    onNavigateToGroupCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text("CheckMate", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        // 그룹 목록
        Button(
            onClick = {
                onClose()
                onNavigateToGroupList()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("그룹 목록")
        }

        // 그룹 생성
        Button(
            onClick = {
                onClose()
                onNavigateToGroupCreate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("그룹 생성")
        }

        // 다크모드 토글
        Button(
            onClick = { onToggleDarkMode(!isDarkMode) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(if (isDarkMode) "라이트 모드" else "다크 모드")
        }

        Spacer(Modifier.weight(1f))

        // 로그아웃
        Button(
            onClick = {
                onClose()
                onLogoutClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("로그아웃")
        }
    }
}
