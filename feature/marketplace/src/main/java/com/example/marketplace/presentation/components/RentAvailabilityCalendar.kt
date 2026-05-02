package com.example.marketplace.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class RentCalendarDayUi(
    val date: String,
    val isAvailable: Boolean
)

@Composable
fun RentAvailabilityCalendar(
    availability: List<RentCalendarDayUi>,
    modifier: Modifier = Modifier,
    selectedStartDate: String? = null,
    selectedEndDate: String? = null,
    onDateClick: ((String) -> Unit)? = null,
    initialCalendar: Calendar = Calendar.getInstance()
) {
    var calendar by remember {
        mutableStateOf(initialCalendar)
    }

    val availabilityByDate = remember(availability) {
        availability.associateBy { it.date }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalendarHeader(
                calendar = calendar,
                onPreviousMonthClick = {
                    calendar = (calendar.clone() as Calendar).apply {
                        add(Calendar.MONTH, -1)
                    }
                },
                onNextMonthClick = {
                    calendar = (calendar.clone() as Calendar).apply {
                        add(Calendar.MONTH, 1)
                    }
                }
            )

            RentCalendarMonthGrid(
                calendar = calendar,
                availabilityByDate = availabilityByDate,
                selectedStartDate = selectedStartDate,
                selectedEndDate = selectedEndDate,
                onDateClick = onDateClick
            )
        }
    }
}

@Composable
private fun CalendarHeader(
    calendar: Calendar,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val canGoToPreviousMonth = canNavigateToPreviousMonth(calendar)

        IconButton(
            enabled = canGoToPreviousMonth,
            onClick = onPreviousMonthClick
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = "Предыдущий месяц",
                tint = if (canGoToPreviousMonth) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
                }
            )
        }

        Text(
            text = formatMonthTitle(calendar),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(onClick = onNextMonthClick) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Следующий месяц"
            )
        }
    }
}

@Composable
private fun RentCalendarMonthGrid(
    calendar: Calendar,
    availabilityByDate: Map<String, RentCalendarDayUi>,
    selectedStartDate: String?,
    selectedEndDate: String?,
    onDateClick: ((String) -> Unit)?
) {
    val days = remember(calendar, availabilityByDate) {
        buildCalendarMonthDays(calendar)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        days.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    if (date == null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                        )
                    } else {
                        val dayAvailability = availabilityByDate[date]
                        val isAvailable = dayAvailability?.isAvailable ?: true
                        val isPast = isPastDate(date)
                        val isSelectable = onDateClick != null && isAvailable && !isPast

                        RentCalendarDayCell(
                            date = date,
                            dayNumber = formatDayNumber(date),
                            isAvailable = isAvailable,
                            isPast = isPast,
                            isSelectedStart = date == selectedStartDate,
                            isSelectedEnd = date == selectedEndDate,
                            isInSelectedRange = isDateInRange(
                                date = date,
                                startDate = selectedStartDate,
                                endDate = selectedEndDate
                            ),
                            isSelectable = isSelectable,
                            onDateClick = {
                                onDateClick?.invoke(date)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RentCalendarDayCell(
    date: String,
    dayNumber: String,
    isAvailable: Boolean,
    isPast: Boolean,
    isSelectedStart: Boolean,
    isSelectedEnd: Boolean,
    isInSelectedRange: Boolean,
    isSelectable: Boolean,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectedEdge = isSelectedStart || isSelectedEnd

    val backgroundColor = when {
        isSelectedEdge -> MaterialTheme.colorScheme.primary
        isInSelectedRange -> MaterialTheme.colorScheme.secondaryContainer
        isPast -> MaterialTheme.colorScheme.surfaceVariant
        !isAvailable -> Color(0xFFFCE1E1)
        else -> Color(0xFFDDFBE9)
    }

    val textColor = when {
        isSelectedEdge -> MaterialTheme.colorScheme.onPrimary
        isInSelectedRange -> MaterialTheme.colorScheme.onSecondaryContainer
        isPast -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
        !isAvailable -> Color(0xFF7A2E2E)
        else -> Color(0xFF1F7A4D)
    }

    Box(
        modifier = modifier
            .padding(2.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(
                enabled = isSelectable,
                onClick = onDateClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayNumber,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = textColor
        )
    }
}

private fun formatDayNumber(date: String): String {
    return formatDate(date, "d")
}

private fun formatDate(date: String, pattern: String): String {
    return runCatching {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat(pattern, Locale("ru"))

        val parsedDate = inputFormat.parse(date)
        parsedDate?.let(outputFormat::format).orEmpty()
    }.getOrDefault("")
}

private fun formatMonthTitle(calendar: Calendar): String {
    val format = SimpleDateFormat("LLLL yyyy", Locale("ru"))

    return format.format(calendar.time)
        .replaceFirstChar { it.uppercase() }
}

private fun buildCalendarMonthDays(calendar: Calendar): List<String?> {
    val monthCalendar = (calendar.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val firstDayOffset = when (monthCalendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        else -> 6
    }

    val result = mutableListOf<String?>()

    repeat(firstDayOffset) {
        result.add(null)
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    repeat(daysInMonth) { index ->
        val dayCalendar = (monthCalendar.clone() as Calendar).apply {
            set(Calendar.DAY_OF_MONTH, index + 1)
        }

        result.add(dateFormat.format(dayCalendar.time))
    }

    while (result.size % 7 != 0) {
        result.add(null)
    }

    return result
}

private fun isPastDate(date: String): Boolean {
    return runCatching {
        val parsedDate = parseDate(date)

        if (parsedDate == null) {
            false
        } else {
            val dateCalendar = Calendar.getInstance().apply {
                time = parsedDate
                clearTime()
            }

            val todayCalendar = Calendar.getInstance().apply {
                clearTime()
            }

            dateCalendar.before(todayCalendar)
        }
    }.getOrDefault(false)
}

private fun canNavigateToPreviousMonth(calendar: Calendar): Boolean {
    val currentCalendar = Calendar.getInstance()

    val selectedYear = calendar.get(Calendar.YEAR)
    val selectedMonth = calendar.get(Calendar.MONTH)

    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)

    return selectedYear > currentYear ||
            selectedYear == currentYear && selectedMonth > currentMonth
}

private fun isDateInRange(
    date: String,
    startDate: String?,
    endDate: String?
): Boolean {
    if (startDate == null || endDate == null) return false

    val current = parseDate(date)?.time ?: return false
    val start = parseDate(startDate)?.time ?: return false
    val end = parseDate(endDate)?.time ?: return false

    return current in start..end
}

private fun parseDate(date: String): java.util.Date? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return inputFormat.parse(date)
}

private fun Calendar.clearTime() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}