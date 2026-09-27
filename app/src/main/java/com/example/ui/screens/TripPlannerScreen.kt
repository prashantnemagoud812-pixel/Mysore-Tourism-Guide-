package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GeneratedDayPlan
import com.example.data.model.ItinerarySlot
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlannerScreen(
    viewModel: MysoreViewModel,
    onBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val uiState by viewModel.uiState.collectAsState()
    val daysOptions = listOf(1, 2, 3, 4)
    val interestOptions = listOf(
        "Heritage", "Food", "Nature", "Shopping", "Photography", "Family", "Adventure"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MidnightNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "AI Trip Planner",
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
                actions = {
                    IconButton(onClick = { onNavigate(AppScreen.SAVED) }) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Saved Trips",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuration Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x33FFD700)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MysoreGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Build Your Perfect Mysore Trip",
                                style = MaterialTheme.typography.titleMedium,
                                color = WarmIvory,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Question 1: How many days?
                        Text(
                            text = "How many days are you staying?",
                            color = MysoreGoldLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            daysOptions.forEach { days ->
                                val isSelected = uiState.plannerDays == days
                                val label = if (days == 4) "4+ Days" else "$days ${if (days == 1) "Day" else "Days"}"
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) MysoreGold else Color(0x2610162B))
                                        .border(
                                            1.dp,
                                            if (isSelected) MysoreGold else GlassBorderGold,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.setPlannerDays(days) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) MidnightNavy else WarmIvory,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Question 2: What do you love?
                        Text(
                            text = "What do you love?",
                            color = MysoreGoldLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Interest Tags Flow
                        val chunked = interestOptions.chunked(3)
                        chunked.forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { interest ->
                                    val isChecked = uiState.activeInterests.contains(interest)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isChecked) RoyalPurpleLight else Color(0x1F2B3654))
                                            .border(
                                                1.dp,
                                                if (isChecked) RoyalPurpleLight else Color(0x33FFD700),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .clickable { viewModel.toggleInterest(interest) }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "${if (isChecked) "✓ " else "+ "}$interest",
                                            color = if (isChecked) MidnightNavy else WarmIvory,
                                            fontSize = 12.sp,
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Generate Button
                        Button(
                            onClick = {
                                viewModel.generateTripItinerary(
                                    uiState.plannerDays,
                                    uiState.activeInterests
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("generate_itinerary_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MysoreGold,
                                contentColor = MidnightNavy
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate My Itinerary",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Results Header & Save Action
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CUSTOM ITINERARY",
                            style = MaterialTheme.typography.labelSmall,
                            color = MysoreGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "${uiState.plannerDays}-Day Curated Mysore Journey",
                            style = MaterialTheme.typography.titleLarge,
                            color = WarmIvory,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { viewModel.saveCurrentItinerary() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalPurple,
                            contentColor = WarmIvory
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("save_itinerary_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Save Trip", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Render Day Plans
            items(uiState.generatedPlan) { dayPlan ->
                DayPlanCard(dayPlan = dayPlan)
            }
        }
    }
}

@Composable
fun DayPlanCard(
    dayPlan: GeneratedDayPlan,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Day Header Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MysoreGold)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "DAY ${dayPlan.dayNumber}",
                        color = MidnightNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = dayPlan.themeTitle,
                    color = WarmIvory,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time Slots Timeline
            dayPlan.slots.forEachIndexed { index, slot ->
                TimelineSlotItem(slot = slot, isLast = index == dayPlan.slots.size - 1)
            }
        }
    }
}

@Composable
fun TimelineSlotItem(
    slot: ItinerarySlot,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Timeline column with icon & connector
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0x33FFD700))
                    .border(1.dp, MysoreGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slot.category.iconEmoji,
                    fontSize = 11.sp
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(84.dp)
                        .background(GlassBorderGold)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 4.dp else 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = slot.timeSlot,
                    color = MysoreGoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "⏱ ${slot.duration}",
                    color = WarmIvoryMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = slot.placeName,
                color = WarmIvory,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = slot.description,
                color = WarmIvoryMuted,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Local Tip
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x280D152D))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "💡 ", fontSize = 11.sp)
                Text(
                    text = slot.travelTip,
                    color = MysoreGoldLight,
                    fontSize = 11.sp
                )
            }
        }
    }
}
