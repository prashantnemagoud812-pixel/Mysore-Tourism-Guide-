package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.BookmarkEntity
import com.example.data.local.ItineraryEntity
import com.example.data.model.Attraction
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedTripsScreen(
    viewModel: MysoreViewModel,
    onBack: () -> Unit,
    onAttractionClick: (Attraction) -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val uiState by viewModel.uiState.collectAsState()
    val bookmarks = uiState.bookmarks
    val itineraries = uiState.savedItineraries
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Itineraries, 1: Bookmarked Places

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MidnightNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Saved Travels",
                        color = WarmIvory,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MysoreGold
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MidnightNavyLight
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MidnightNavyLight,
                contentColor = MysoreGold
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Trips (${itineraries.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Bookmarks (${bookmarks.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                // Itineraries Tab
                if (itineraries.isEmpty()) {
                    EmptySavedState(
                        icon = Icons.Default.EventNote,
                        title = "No Saved Trips Yet",
                        subtitle = "Generate your personalized Mysore itinerary using the AI Trip Planner and save it here for offline access.",
                        buttonLabel = "Create Itinerary",
                        onButtonClick = { onNavigate(AppScreen.PLANNER) }
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(itineraries, key = { it.id }) { item ->
                            SavedItineraryCard(
                                itinerary = item,
                                onDelete = { viewModel.deleteSavedItinerary(item.id) },
                                onView = { onNavigate(AppScreen.PLANNER) }
                            )
                        }
                    }
                }
            } else {
                // Bookmarked Places Tab
                if (bookmarks.isEmpty()) {
                    EmptySavedState(
                        icon = Icons.Default.BookmarkBorder,
                        title = "No Bookmarked Places",
                        subtitle = "Tap the bookmark icon on any palace, temple, garden, or food destination to save it for quick reference.",
                        buttonLabel = "Explore Places",
                        onButtonClick = { onNavigate(AppScreen.EXPLORE) }
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(bookmarks, key = { it.id }) { bookmark ->
                            SavedBookmarkCard(
                                bookmark = bookmark,
                                onClick = {
                                    val attr = viewModel.attractions.find { it.id == bookmark.id }
                                    if (attr != null) onAttractionClick(attr)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedItineraryCard(
    itinerary: ItineraryEntity,
    onDelete: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateStr = remember(itinerary.createdAt) {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(itinerary.createdAt))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, GlassBorderGold, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x33FFD700))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${itinerary.daysCount} DAYS",
                        color = MysoreGoldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = WarmIvoryMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = itinerary.title,
                style = MaterialTheme.typography.titleMedium,
                color = WarmIvory,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Saved on $dateStr • Interests: ${itinerary.interestsCsv}",
                style = MaterialTheme.typography.bodySmall,
                color = MysoreGoldLight,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = itinerary.contentSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = WarmIvoryMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onView,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MysoreGold,
                    contentColor = MidnightNavy
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "View Schedule", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SavedBookmarkCard(
    bookmark: BookmarkEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = bookmark.imageUrl,
                contentDescription = bookmark.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookmark.title,
                    color = WarmIvory,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${bookmark.category} • ${bookmark.location}",
                    color = WarmIvoryMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "★ ${bookmark.rating}",
                    color = MysoreGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MysoreGold
            )
        }
    }
}

@Composable
fun EmptySavedState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    buttonLabel: String,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0x28FFD700)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MysoreGold,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = WarmIvory,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = WarmIvoryMuted,
            lineHeight = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MysoreGold,
                contentColor = MidnightNavy
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = buttonLabel, fontWeight = FontWeight.Bold)
        }
    }
}
