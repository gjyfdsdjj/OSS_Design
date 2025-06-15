package com.example.checkmate.ui.group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkmate.data.GroupRepository
import com.example.checkmate.model.Group
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    onGroupClick: (Group) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }

    // 실시간 반영 (Firestore Listener)
    DisposableEffect(Unit) {
        val listener = GroupRepository.listenGroups { groups = it }
        onDispose { listener.remove() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("그룹 목록") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        if (groups.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("아직 가입한 그룹이 없습니다.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(groups, key = { it.id }) { group ->
                    Card(
                        onClick = { onGroupClick(group) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(text = group.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "생성자: ${group.createdBy}")
                            Text(text = "멤버수: ${group.members.size}")
                        }
                    }
                }
            }
        }
    }
}
