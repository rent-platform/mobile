package com.example.chat.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.components.RentFilterItem
import com.example.ui.components.RentFilterRow
import com.example.ui.components.RentSegmentSwitcher
import com.example.ui.components.renterOwnerSegmentItems
import com.example.ui.theme.RentPlatformTheme

@Composable
fun ChatsScreen(
    uiState: ChatsUiState,
    onRoleClick: (ChatRole) -> Unit,
    onFilterClick: (ChatFilter) -> Unit,
    onChatClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            Text(
                text = "Чаты",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = uiState.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            RentSegmentSwitcher(
                items = renterOwnerSegmentItems(
                    ownerValue = ChatRole.OWNER,
                    renterValue = ChatRole.RENTER
                ),
                selectedValue = uiState.selectedRole,
                onValueClick = onRoleClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            RentFilterRow(
                items = ChatFilter.entries.map { filter ->
                    RentFilterItem(
                        value = filter,
                        title = filter.title
                    )
                },
                selectedValue = uiState.selectedFilter,
                onValueClick = onFilterClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> ChatsLoadingContent()

                uiState.errorMessage != null -> ChatsErrorContent(
                    message = uiState.errorMessage,
                    onRetryClick = onRetryClick
                )

                uiState.visibleChats.isEmpty() -> ChatsEmptyContent(
                    selectedFilter = uiState.selectedFilter
                )

                else -> ChatsListContent(
                    chats = uiState.visibleChats,
                    onChatClick = onChatClick
                )
            }
        }
    }
}

@Composable
private fun ChatsListContent(
    chats: List<ChatItemUi>,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = chats,
            key = { chat -> chat.id }
        ) { chat ->
            ChatCard(
                chat = chat,
                onClick = { onChatClick(chat.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ChatCard(
    chat: ChatItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            ChatItemImage(
                imageResId = chat.imageResId,
                title = chat.announcementTitle,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = chat.authorNickname,
                            modifier = Modifier.weight(
                                weight = 1f,
                                fill = false
                            ),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        AuthorAvatar(
                            avatarUrl = chat.authorAvatarUrl,
                            nickname = chat.authorNickname,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = chat.lastMessageTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chat.announcementTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                ChatStatusBadge(status = chat.orderStatus)
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (chat.unreadCount > 0) {
                UnreadBadge(unreadCount = chat.unreadCount)
            }
        }
    }
}

@Composable
private fun AuthorAvatar(
    avatarUrl: String?,
    nickname: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl.isNullOrBlank()) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = nickname.firstOrNull()
                            ?.uppercase()
                            .orEmpty(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1
                    )
                }
            }
        } else {
            AsyncImage(
                model = avatarUrl,
                contentDescription = nickname,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ChatItemImage(
    imageResId: Int?,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (imageResId == null) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.ImageNotSupported,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun UnreadBadge(
    unreadCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(24.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ChatStatusBadge(
    status: ChatOrderStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = status.containerColor()
    ) {
        Text(
            text = status.title,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = status.contentColor(),
            maxLines = 1
        )
    }
}

@Composable
private fun ChatOrderStatus.containerColor(): Color {
    return when (this) {
        ChatOrderStatus.REQUEST -> MaterialTheme.colorScheme.tertiaryContainer
        ChatOrderStatus.CONFIRMED -> MaterialTheme.colorScheme.primaryContainer
        ChatOrderStatus.IN_RENT -> MaterialTheme.colorScheme.secondaryContainer
        ChatOrderStatus.COMPLETED -> MaterialTheme.colorScheme.surface
        ChatOrderStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
    }
}

@Composable
private fun ChatOrderStatus.contentColor(): Color {
    return when (this) {
        ChatOrderStatus.REQUEST -> MaterialTheme.colorScheme.onTertiaryContainer
        ChatOrderStatus.CONFIRMED -> MaterialTheme.colorScheme.onPrimaryContainer
        ChatOrderStatus.IN_RENT -> MaterialTheme.colorScheme.onSecondaryContainer
        ChatOrderStatus.COMPLETED -> MaterialTheme.colorScheme.onSurface
        ChatOrderStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
    }
}

@Composable
private fun ChatsLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ChatsErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = onRetryClick) {
                Text(text = "Повторить")
            }
        }
    }
}

@Composable
private fun ChatsEmptyContent(
    selectedFilter: ChatFilter,
    modifier: Modifier = Modifier
) {
    val title = when (selectedFilter) {
        ChatFilter.ALL -> "Чатов пока нет"
        ChatFilter.UNREAD -> "Нет непрочитанных чатов"
    }

    val description = when (selectedFilter) {
        ChatFilter.ALL -> "Когда появится переписка по аренде вещи, она будет отображаться здесь."
        ChatFilter.UNREAD -> "Все сообщения уже прочитаны."
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}