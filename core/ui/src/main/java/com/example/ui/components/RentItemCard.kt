package com.example.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RentItemCard(
    title: String,
    location: String,
    pricePerDay: String?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes imageResId: Int? = null,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                if (imageResId == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Фото",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 2.dp)
                        .size(26.dp)
                        .clickable(onClick = onFavoriteClick),
                    contentAlignment = Alignment.Center
                ) {
                    if (isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Убрать из избранного",
                            tint = Color.Red,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize()
                        )

                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Добавить в избранное",
                            tint = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                pricePerDay?.let { price ->
                    Surface(
                        modifier = Modifier.offset(x = (-6).dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = price,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (location.isNotBlank()) {
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}