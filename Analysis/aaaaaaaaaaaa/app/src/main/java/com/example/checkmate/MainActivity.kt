package com.example.checkmate

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.checkmate.ui.theme.CheckMateTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import java.util.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text



data class ToDoItem(val task: String, val date: String)

fun groupTasksByDate(taskList: List<ToDoItem>): Map<String, List<ToDoItem>> {
    return taskList.groupBy { it.date }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckMateTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf("Login") }
    val screens = listOf(
        "Login", "Register", "To-Do List", "Group Members",
        "Weather Notification", "Group Task Status",
        "Share To-Do List", "Check Group Task"
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, // 상단 정렬
        horizontalAlignment = Alignment.CenterHorizontally // 가로 중앙 정렬
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(screens.size) { index ->
                Button(
                    onClick = { selectedScreen = screens[index] },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(screens[index], fontSize = 12.sp)
                }
            }
        }

        when (selectedScreen) {
            "Login" -> LoginScreen()
            "Register" -> RegisterScreen()
            "To-Do List" -> ToDoListScreen()
            "Group Members" -> GroupMemberScreen()
            "Weather Notification" -> WeatherNotificationScreen()
            "Group Task Status" -> GroupTaskStatusScreen()
            "Share To-Do List" -> ShareToDoScreen()
            "Check Group Task" -> CheckGroupTaskScreen()
        }

    }
}

@Composable
fun LoginScreen() {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, // 상단 정렬
        horizontalAlignment = Alignment.CenterHorizontally ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Button(onClick = { /* Do login */ }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Login")
        }
    }
}

@Composable
fun RegisterScreen() {
    var newId by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, // 상단 정렬
        horizontalAlignment = Alignment.CenterHorizontally ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = newId, onValueChange = { newId = it }, label = { Text("New ID") })
        OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("New Password") })
        Button(onClick = {}, modifier = Modifier.padding(top = 8.dp)) {
            Text("Register")
        }
    }
}

@Composable
fun ToDoListScreen() {
    var task by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDate by remember { mutableStateOf("") }
    val taskList = remember { mutableStateListOf<ToDoItem>() }

    var editingIndex by remember { mutableStateOf(-1) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "${year}-${month + 1}-${dayOfMonth}"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("To-Do List", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = task, onValueChange = { task = it }, label = { Text("New Task") })
        Spacer(Modifier.height(8.dp))

        Row {
            Button(onClick = { datePickerDialog.show() }) {
                Text(if (selectedDate.isEmpty()) "Pick Date" else selectedDate)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            if (task.text.isNotBlank() && selectedDate.isNotBlank()) {
                if (editingIndex >= 0) {
                    taskList[editingIndex] = ToDoItem(task.text, selectedDate)
                    editingIndex = -1
                } else {
                    taskList.add(ToDoItem(task.text, selectedDate))
                }
                task = TextFieldValue("")
                selectedDate = ""
            }
        }) {
            Text(if (editingIndex >= 0) "Update Task" else "Add Task")
        }

        Spacer(Modifier.height(8.dp))

        val groupedTasks = groupTasksByDate(taskList)
        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            items(daysOfWeek.size) { index ->
                Text(daysOfWeek[index], modifier = Modifier.padding(8.dp))
            }

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            val firstDayCalendar = Calendar.getInstance()
            firstDayCalendar.set(year, month, 1)
            val firstDayOfWeek = firstDayCalendar.get(Calendar.DAY_OF_WEEK)
            val totalCells = daysInMonth + (firstDayOfWeek - 1)

            items(totalCells) { index ->
                if (index < firstDayOfWeek - 1) {
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .height(8.dp)
                        .width(40.dp))
                } else {
                    val day = index - (firstDayOfWeek - 1) + 1
                    val formattedDate = "$year-${month + 1}-$day"
                    val tasksForDay = groupedTasks[formattedDate] ?: emptyList()

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(4.dp)) {
                        Text("$day", style = MaterialTheme.typography.bodySmall)
                        tasksForDay.forEachIndexed { idx, item ->
                            val globalIndex = taskList.indexOfFirst { it == item }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(item.task, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    task = TextFieldValue(item.task)
                                    selectedDate = item.date
                                    editingIndex = globalIndex
                                }) {
                                    Text("✎", fontSize = 12.sp)
                                }
                                IconButton(onClick = {
                                    taskList.removeAt(globalIndex)
                                }) {
                                    Text("🗑", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun GroupMemberScreen() {
    var email by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf("member1@example.com", "member2@example.com") }
    var editingIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Group Members", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Add/Update Email") })
        Button(onClick = {
            if (email.isNotBlank()) {
                if (editingIndex >= 0) {
                    members[editingIndex] = email
                    editingIndex = -1
                } else {
                    members.add(email)
                }
                email = ""
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text(if (editingIndex >= 0) "Update Member" else "Add Member")
        }

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(members.size) { index ->
                val member = members[index]
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(member, fontSize = 16.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        email = member
                        editingIndex = index
                    }) {
                        Text("✎", fontSize = 14.sp)
                    }
                    IconButton(onClick = {
                        members.removeAt(index)
                    }) {
                        Text("🗑", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherNotificationScreen() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, // 상단 정렬
        horizontalAlignment = Alignment.CenterHorizontally ) {
        Text("Weather Notification", style = MaterialTheme.typography.headlineMedium)
        Text("Rain expected at 3 PM", fontSize = 16.sp)
    }
}

@Composable
fun GroupTaskStatusScreen() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, // 상단 정렬
        horizontalAlignment = Alignment.CenterHorizontally ) {
        Text("Group Task Status", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(listOf("John: Done", "Jane: In Progress", "Paul: Not Started")) {
                Text(it, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ShareToDoScreen() {
    var email by remember { mutableStateOf("") }
    var task by remember { mutableStateOf("") }
    val sharedTasks = remember { mutableStateListOf<Pair<String, String>>() }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Share To-Do List", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Recipient Email") })
        OutlinedTextField(value = task, onValueChange = { task = it }, label = { Text("Task to Share") })

        Button(
            onClick = {
                if (email.isNotBlank() && task.isNotBlank()) {
                    sharedTasks.add(Pair(email, task))
                    email = ""
                    task = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Share Task")
        }

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(sharedTasks.size) { index ->
                val (to, t) = sharedTasks[index]
                Text("Sent to $to: \"$t\"", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun CheckGroupTaskScreen() {
    val groupTasks = listOf(
        "Alice - Task A: Done",
        "Bob - Task B: In Progress",
        "Charlie - Task C: Not Started"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Check Group Task", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(groupTasks.size) { index ->
                Text(groupTasks[index], fontSize = 16.sp)
            }
        }
    }
}

