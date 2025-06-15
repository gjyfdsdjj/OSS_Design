package com.example.checkmate.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.checkmate.data.*
import com.example.checkmate.model.*
import com.example.checkmate.ui.component.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.kizitonwose.calendar.compose.rememberCalendarState
import kotlinx.coroutines.launch
import java.time.*
import java.util.*

fun Timestamp.toLocalDate(): LocalDate {
    return this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

enum class FilterType { ALL, PENDING, COMPLETED }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTodoScreen(
    onLogout: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onNavigateToGroupList: () -> Unit,
    onNavigateToGroupCreate: () -> Unit
) {
    val userId = AuthManager.currentUserId()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var todos by remember { mutableStateOf<List<ToDoItem>>(emptyList()) }
    var newTodoText by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var editingTodoId by remember { mutableStateOf<String?>(null) }
    var editedText by remember { mutableStateOf("") }
    var forecast by remember { mutableStateOf<List<ForecastEntry>>(emptyList()) }
    var filter by remember { mutableStateOf(FilterType.ALL) }

    val calendarState = rememberCalendarState(
        startMonth = YearMonth.now().minusMonths(12),
        endMonth = YearMonth.now().plusMonths(12),
        firstVisibleMonth = YearMonth.now(),
        firstDayOfWeek = DayOfWeek.SUNDAY
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var todoListener by remember { mutableStateOf<ListenerRegistration?>(null) }

    DisposableEffect(userId) {
        if (userId != null) {
            todoListener = ToDoRepository.listenToToDos(userId) { list -> todos = list }
        }
        onDispose { todoListener?.remove() }
    }

    // 날씨 API
    LaunchedEffect(Unit) {
        try {
            val response = WeatherRepository.getWeather()
            Log.d("WEATHER_UI", "forecast list size: ${response?.list?.size}")
            forecast = response?.list ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            forecast = emptyList()
        }
    }

    val todosByDate = todos.groupBy { it.dueDate?.toLocalDate() }

    val filteredTodos = todos
        .filter { it.dueDate?.toLocalDate() == selectedDate }
        .filter {
            when (filter) {
                FilterType.ALL -> true
                FilterType.PENDING -> !it.completed
                FilterType.COMPLETED -> it.completed
            }
        }
        .sortedBy {
            when (Priority.fromString(it.priority)) {
                Priority.HIGH -> 0
                Priority.MEDIUM -> 1
                Priority.LOW -> 2
            }
        }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onLogoutClick = onLogout,
                onClose = { coroutineScope.launch { drawerState.close() } },
                onNavigateToGroupList = onNavigateToGroupList,
                onNavigateToGroupCreate = onNavigateToGroupCreate
            )
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.padding(16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
                    }

                    Text("캘린더 할 일", style = MaterialTheme.typography.headlineMedium)
                }

                Spacer(Modifier.height(12.dp))

                CalendarHeader(calendarState, coroutineScope)

                CalendarView(
                    calendarState = calendarState,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    todosByDate = todosByDate
                )

                Spacer(Modifier.height(8.dp))

                Text("선택 날짜: $selectedDate", style = MaterialTheme.typography.titleMedium)


                WeatherSummary(forecasts = forecast, selectedDate = selectedDate)

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterType.entries.forEach { type ->
                        FilterChip(
                            selected = filter == type,
                            onClick = { filter = type },
                            label = { Text(type.name) }
                        )
                    }
                }

                AddTodoSection(
                    newTodoText = newTodoText,
                    onTextChange = { newTodoText = it },
                    selectedPriority = selectedPriority,
                    onPriorityChange = { selectedPriority = it },
                    onAddClick = {
                        if (userId != null && newTodoText.isNotBlank()) {
                            val todo = ToDoItem(
                                title = newTodoText,
                                dueDate = Timestamp(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())),
                                priority = selectedPriority.name
                            )
                            ToDoRepository.addToDo(userId, todo) { success ->
                                if (success) newTodoText = ""
                            }
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(filteredTodos, key = { it.id }) { todo ->
                        val isEditing = editingTodoId == todo.id
                        TodoItemRow(
                            todo = todo,
                            isEditing = isEditing,
                            editedText = if (isEditing) editedText else "",
                            onCheckedChange = { isChecked ->
                                val updated = todo.copy(completed = isChecked)
                                ToDoRepository.updateToDo(userId!!, updated)
                            },
                            onEditClick = {
                                editingTodoId = todo.id
                                editedText = todo.title
                            },
                            onDeleteClick = {
                                ToDoRepository.deleteToDo(userId!!, todo.id)
                            },
                            onEditTextChange = { editedText = it },
                            onEditSave = {
                                val updated = todo.copy(title = editedText)
                                ToDoRepository.updateToDo(userId!!, updated)
                                editingTodoId = null
                            }
                        )
                    }
                }

                if (filteredTodos.isEmpty()) {
                    EmptyMessage()
                }
            }
        }
    }
}
