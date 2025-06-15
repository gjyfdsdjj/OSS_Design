package com.example.checkmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.checkmate.model.ToDoItem
import com.example.checkmate.ui.toLocalDate
import com.google.firebase.Timestamp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate

@Composable
fun CalendarView(
    calendarState: CalendarState,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    todosByDate: Map<LocalDate?, List<ToDoItem>>
) {
    HorizontalCalendar(
        state = calendarState,
        dayContent = { day ->
            val date = day.date
            val hasTodo = todosByDate.containsKey(date)
            val isSelected = date == selectedDate && day.position == DayPosition.MonthDate
            val isToday = date == LocalDate.now()

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        if (day.position == DayPosition.MonthDate) onDateSelected(date)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        day.position != DayPosition.MonthDate -> MaterialTheme.colorScheme.outline
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier
                        .background(
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                isToday -> MaterialTheme.colorScheme.secondaryContainer
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )

                if (hasTodo) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    )
}
