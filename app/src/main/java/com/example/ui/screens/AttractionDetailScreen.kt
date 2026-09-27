package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Attraction
import com.example.ui.components.AudioGuidePlayer
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionDetailScreen(
    attraction: Attraction,
    viewModel: MysoreViewModel,
    onBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val uiState by viewModel.uiState.collectAsState()
    val isBookmarked = uiState.bookmarks.any { it.id == attraction.id }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MidnightNavy,
        bottomBar = {
            // Sticky Action Bar
            Surface(
                color = MidnightNavyLight,
                tonalElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderGold),
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.MAP) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MysoreGold),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MysoreGold),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "View on Map", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onNavigate(AppScreen.PLANNER) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_add_to_trip_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MysoreGold,
                            contentColor = MidnightNavy
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Plan My Visit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image Header with Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = attraction.imageUrl,
                    contentDescription = attraction.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Transparent,
                                    MidnightNavy
                                )
                            )
                        )
                )

                // Navigation Back Button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xB3090D1A))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MysoreGold
                    )
                }

                // Bookmark Button
                IconButton(
                    onClick = { viewModel.toggleBookmark(attraction) },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xB3090D1A))
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isBookmarked) MysoreGold else WarmIvory
                    )
                }

                // Bottom Titles on Hero
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xD9FFD700))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${attraction.category.iconEmoji} ${attraction.category.displayName}",
                                color = MidnightNavy,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (attraction.isIlluminatedAtNight) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xD98E24AA))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✨ 100k Bulbs Lighting",
                                    color = WarmIvory,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = attraction.name,
                        style = MaterialTheme.typography.displayMedium,
                        color = WarmIvory,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = attraction.tagLine,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MysoreGoldLight
                    )
                }
            }

            // Body Content
            Column(modifier = Modifier.padding(16.dp)) {
                // Key Metrics Row (Rating, Fee, Duration, Distance)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightNavyCard)
                        .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    DetailFactItem(
                        icon = Icons.Default.Star,
                        label = "Rating",
                        value = "${attraction.rating} ★"
                    )
                    DetailFactItem(
                        icon = Icons.Default.Schedule,
                        label = "Duration",
                        value = attraction.durationText
                    )
                    DetailFactItem(
                        icon = Icons.Default.ConfirmationNumber,
                        label = "Entry",
                        value = attraction.entryFee.split("/").firstOrNull() ?: attraction.entryFee
                    )
                    DetailFactItem(
                        icon = Icons.Default.NearMe,
                        label = "Distance",
                        value = "${attraction.distanceKm} km"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Audio Guide Narration Player
                AudioGuidePlayer(
                    audioSnippet = attraction.audioSnippet,
                    title = attraction.name
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Practical Timings & Best Visit Info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, GlassBorderGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = MysoreGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Timings & Visiting Tips",
                                color = WarmIvory,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "🕒 Visiting Hours: ${attraction.timings}",
                            color = WarmIvory,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 Best Time to Visit: ${attraction.bestTimeToVisit}",
                            color = MysoreGoldLight,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📍 Address: ${attraction.location}",
                            color = WarmIvoryMuted,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Description
                Text(
                    text = "About This Heritage Landmark",
                    style = MaterialTheme.typography.titleLarge,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = attraction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = WarmIvoryMuted,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Historical Highlights
                Text(
                    text = "Key Royal Highlights",
                    style = MaterialTheme.typography.titleLarge,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(10.dp))

                attraction.historicalHighlights.forEach { highlight ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "✦",
                            color = MysoreGold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = highlight,
                            color = WarmIvory,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DetailFactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MysoreGold,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = WarmIvory,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = WarmIvoryMuted,
            fontSize = 10.sp
        )
    }
}
