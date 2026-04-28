package com.example.marketplace.presentation.itemdetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RentPrimaryButton
import com.example.marketplace.presentation.components.formatPricePerDay
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.example.marketplace.presentation.components.formatPricePerHour
import com.example.marketplace.R
import com.example.marketplace.presentation.components.formatPublishedDate
import com.example.ui.theme.RentPlatformTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.width
import com.example.marketplace.presentation.catalog.CatalogItemUi
import com.example.marketplace.presentation.components.MarketplaceItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    uiState: ItemDetailsUiState,
    onEvent: (ItemDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onEvent(ItemDetailsEvent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(ItemDetailsEvent.OnFavoriteClick) }) {
                        Icon(
                            imageVector = if (uiState.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Добавить в избранное"
                        )
                    }

                    IconButton(onClick = { onEvent(ItemDetailsEvent.OnShareClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            RentBottomBar(
                onRentClick = { onEvent(ItemDetailsEvent.OnRentClick) }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding()),
                    contentPadding = PaddingValues(
                        bottom = innerPadding.calculateBottomPadding()
                    )
                ) {
                    item {
                        ItemImagesPager(
                            imageResIds = uiState.imageResIds
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = uiState.title,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            uiState.pricePerDay?.let { price ->
                                Text(
                                    text = formatPricePerDay(price),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            uiState.pricePerHour?.let { price ->
                                Text(
                                    text = formatPricePerHour(price),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (uiState.createdAt.isNotBlank()) {
                                Text(
                                    text = "Опубликовано: ${formatPublishedDate(uiState.createdAt)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            uiState.depositAmount
                                ?.takeIf { it > 0 }
                                ?.let { deposit ->
                                    DepositBlock(
                                        depositAmount = deposit,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                        }
                    }
                    item {
                        LocationBlock(
                            city = uiState.city,
                            pickupLocation = uiState.pickupLocation,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                    item {
                        AvailabilityBlock(
                            availability = uiState.availability,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    item {
                        DescriptionBlock(
                            description = uiState.description,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                        )
                    }

                    item {
                        OwnerBlock(
                            ownerName = uiState.ownerName,
                            ownerRating = uiState.ownerRating,
                            reviewsCount = uiState.reviewsCount,
                            onAskOwnerClick = {
                                // позже ItemDetailsEvent.OnAskOwnerClick
                            },
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                    item {
                        SimilarItemsBlock(
                            items = uiState.similarItems,
                            onItemClick = { itemId ->
                                onEvent(ItemDetailsEvent.OnSimilarItemClick(itemId))
                            },
                            onFavoriteClick = { itemId ->
                                onEvent(ItemDetailsEvent.OnSimilarFavoriteClick(itemId))
                            },
                            onSeeMoreClick = {
                                onEvent(ItemDetailsEvent.OnSimilarSeeMoreClick)
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemImagesPager(
    imageResIds: List<Int>,
    modifier: Modifier = Modifier
) {
    if (imageResIds.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageResIds.size }
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val pagerHeight = screenHeight * 0.38f

    Box(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(pagerHeight)
        ) { page ->
            Image(
                painter = painterResource(id = imageResIds[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                contentScale = ContentScale.Crop
            )
        }

        if (imageResIds.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(imageResIds.size) { index ->
                    Surface(
                        modifier = Modifier.size(
                            width = if (pagerState.currentPage == index) 18.dp else 8.dp,
                            height = 8.dp
                        ),
                        shape = RoundedCornerShape(50),
                        color = if (pagerState.currentPage == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {}
                }
            }
        }
    }
}

@Composable
private fun DepositBlock(
    depositAmount: Long,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Залог",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = formatDepositAmount(depositAmount),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun LocationBlock(
    city: String,
    pickupLocation: String?,
    modifier: Modifier = Modifier
) {
    if (city.isBlank() && pickupLocation.isNullOrBlank()) return

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Местоположение",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (city.isNotBlank()) {
                Text(
                    text = city,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!pickupLocation.isNullOrBlank()) {
                Text(
                    text = pickupLocation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun AvailabilityBlock(
    availability: List<ItemAvailabilityDayUiState>,
    modifier: Modifier = Modifier
) {
    if (availability.isEmpty()) return

    var isCalendarDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Доступность",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availability.take(8)) { day ->
                AvailabilityDayCard(day = day)
            }
        }

        Text(
            text = "Посмотреть ещё",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable {
                    isCalendarDialogVisible = true
                }
        )
    }

    if (isCalendarDialogVisible) {
        AvailabilityCalendarDialog(
            availability = availability,
            onDismiss = {
                isCalendarDialogVisible = false
            }
        )
    }
}

@Composable
private fun AvailabilityDayCard(
    day: ItemAvailabilityDayUiState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (day.isAvailable) {
        Color(0xFFDDFBE9)
    } else {
        Color(0xFFFCE1E1)
    }

    val textColor = if (day.isAvailable) {
        Color(0xFF1F7A4D)
    } else {
        Color(0xFF7A2E2E)
    }

    Column(
        modifier = modifier
            .size(width = 74.dp, height = 98.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatDayOfWeek(day.date),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )

        Text(
            text = formatDayNumber(day.date),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )

        Text(
            text = formatShortMonth(day.date),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )

        Text(
            text = if (day.isAvailable) "свободен" else "занят",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor
        )
    }
}

@Composable
private fun AvailabilityCalendarDialog(
    availability: List<ItemAvailabilityDayUiState>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        AvailabilityCalendarContent(
            availability = availability,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun AvailabilityCalendarContent(
    availability: List<ItemAvailabilityDayUiState>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        calendar = (calendar.clone() as Calendar).apply {
                            add(Calendar.MONTH, -1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "Предыдущий месяц"
                    )
                }

                Text(
                    text = formatMonthTitle(calendar),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                IconButton(
                    onClick = {
                        calendar = (calendar.clone() as Calendar).apply {
                            add(Calendar.MONTH, 1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = "Следующий месяц"
                    )
                }
            }

            CalendarMonthGrid(
                calendar = calendar,
                availabilityByDate = availabilityByDate
            )

            RentPrimaryButton(
                text = "Ок",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CalendarMonthGrid(
    calendar: Calendar,
    availabilityByDate: Map<String, ItemAvailabilityDayUiState>
) {
    val days = remember(calendar, availabilityByDate) {
        buildCalendarMonthDays(calendar)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { date ->
                    if (date == null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                        )
                    } else {
                        val availability = availabilityByDate[date]
                        val isAvailable = availability?.isAvailable ?: true

                        CalendarDayCell(
                            dayNumber = formatDayNumber(date),
                            isAvailable = isAvailable,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    dayNumber: String,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isAvailable) {
        Color(0xFFDDFBE9)
    } else {
        Color(0xFFFCE1E1)
    }

    val textColor = if (isAvailable) {
        Color(0xFF1F7A4D)
    } else {
        Color(0xFF7A2E2E)
    }

    Box(
        modifier = modifier
            .padding(2.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor),
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

@Composable
private fun DescriptionBlock(
    description: String,
    modifier: Modifier = Modifier
) {
    if (description.isBlank()) return

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Описание",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun OwnerBlock(
    ownerName: String,
    ownerRating: Float?,
    reviewsCount: Int,
    onAskOwnerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (ownerName.isBlank()) return

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Владелец объявления",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDDFBE9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ownerName.first().uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1F7A4D)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = ownerName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (ownerRating != null || reviewsCount > 0) {
                        Text(
                            text = buildString {
                                ownerRating?.let {
                                    append("★ ")
                                    append(it)
                                }

                                if (reviewsCount > 0) {
                                    if (isNotEmpty()) append(" · ")
                                    append(reviewsCount)
                                    append(" отзывов")
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onAskOwnerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Спросить у владельца",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SimilarItemsBlock(
    items: List<CatalogItemUi>,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onSeeMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Похожие объявления",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Посмотреть больше",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onSeeMoreClick)
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = items,
                key = { item -> item.id }
            ) { item ->
                MarketplaceItemCard(
                    item = item,
                    onClick = { onItemClick(item.id) },
                    onFavoriteClick = { onFavoriteClick(item.id) },
                    modifier = Modifier.width(180.dp)
                )
            }
        }
    }
}

private fun formatDayOfWeek(date: String): String {
    return formatDate(date, "EE")
}

private fun formatDayNumber(date: String): String {
    return formatDate(date, "d")
}

private fun formatShortMonth(date: String): String {
    return formatDate(date, "MMM")
        .replace(".", "")
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
private fun formatDepositAmount(price: Long): String {
    return "%,d ₽".format(price).replace(',', ' ')
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

@Composable
private fun RentBottomBar(
    onRentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        RentPrimaryButton(
            text = "Арендовать",
            onClick = onRentClick,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
private fun ItemDetailsScreenPreview() {
    RentPlatformTheme {
        ItemDetailsScreen(
            uiState = ItemDetailsUiState(
                id = "0",
                title = "Пукофон",
                description = "Смартфон в хорошем состоянии, подойдёт для поездки или временного использования.",
                pricePerDay = 999,
                pricePerHour = 150,
                depositAmount = 5000,
                city = "Новосибирск",
                pickupLocation = "ул. Ленина, 10",
                ownerName = "Иван",
                ownerRating = 4.8f,
                reviewsCount = 12,
                createdAt = "2026-04-28T09:59:44.133Z",
                imageResIds = listOf(
                    R.drawable.pocofon,
                    R.drawable.pocofon,
                    R.drawable.pocofon
                ),
                availability = previewAvailability(),
                similarItems = previewSimilarItems()
            ),
            onEvent = {}
        )
    }
}
@Preview(showBackground = true, widthDp = 380)
@Composable
private fun AvailabilityCalendarContentPreview() {
    RentPlatformTheme {
        AvailabilityCalendarContent(
            availability = previewAvailability(),
            onDismiss = {},
            modifier = Modifier.padding(16.dp),
            initialCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2026)
                set(Calendar.MONTH, Calendar.APRIL)
                set(Calendar.DAY_OF_MONTH, 1)
            }
        )
    }
}
private fun previewAvailability(): List<ItemAvailabilityDayUiState> {
    return listOf(
        ItemAvailabilityDayUiState(date = "2026-04-28", isAvailable = true),
        ItemAvailabilityDayUiState(date = "2026-04-29", isAvailable = true),
        ItemAvailabilityDayUiState(date = "2026-04-30", isAvailable = false),
        ItemAvailabilityDayUiState(date = "2026-05-01", isAvailable = false),
        ItemAvailabilityDayUiState(date = "2026-05-02", isAvailable = true),
        ItemAvailabilityDayUiState(date = "2026-05-03", isAvailable = true),
        ItemAvailabilityDayUiState(date = "2026-05-04", isAvailable = false),
        ItemAvailabilityDayUiState(date = "2026-05-05", isAvailable = true)
    )
}
private fun previewSimilarItems(): List<CatalogItemUi> {
    return listOf(
        CatalogItemUi(
            id = "1",
            title = "iPhone 13",
            pricePerDay = 1200,
            location = "Новосибирск",
            imageUrl = R.drawable.pocofon
        ),
        CatalogItemUi(
            id = "2",
            title = "Samsung Galaxy S22",
            pricePerDay = 1000,
            location = "Новосибирск",
            imageUrl = R.drawable.pocofon,
            isFavorite = true
        ),
        CatalogItemUi(
            id = "3",
            title = "PowerBank 20000 mAh",
            pricePerDay = 250,
            location = "Новосибирск",
            imageUrl = R.drawable.pocofon
        ),
        CatalogItemUi(
            id = "4",
            title = "Планшет Xiaomi Pad",
            pricePerDay = 800,
            location = "Новосибирск",
            imageUrl = R.drawable.pocofon
        )
    )
}