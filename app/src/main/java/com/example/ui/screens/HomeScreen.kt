package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel

@Composable
fun HomeScreen(
    viewModel: MysoreViewModel,
    onNavigate: (AppScreen) -> Unit,
    onAttractionClick: (Attraction) -> Unit,
    modifier: Modifier = Modifier
) {
    val attractions = viewModel.attractions
    val foods = viewModel.foods
    val experiences = viewModel.experiences
    val travelInfo = viewModel.travelInfo
    val weather = viewModel.weather
    val markers = viewModel.mapMarkers

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. HERO 3D SECTION
        item {
            Palace3DHeroView(
                onExploreClick = { onNavigate(AppScreen.EXPLORE) },
                onPlanTripClick = { onNavigate(AppScreen.PLANNER) }
            )
        }

        // 2. QUICK DISCOVERY SECTION
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "DISCOVER BY CATEGORY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MysoreGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Everything Mysore Has to Offer",
                    style = MaterialTheme.typography.headlineLarge,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(14.dp))

                // 2x3 Grid of Discovery Categories
                val categories = Category.values()
                for (chunk in categories.toList().chunked(2)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        chunk.forEach { cat ->
                            CategoryCard(
                                category = cat,
                                onClick = {
                                    viewModel.selectCategory(cat)
                                    onNavigate(AppScreen.EXPLORE)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 3. TOP ATTRACTIONS (Horizontal Scrolling on Mobile)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ICONIC MONUMENTS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MysoreGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Places You Shouldn’t Miss",
                            style = MaterialTheme.typography.headlineLarge,
                            color = WarmIvory,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    TextButton(onClick = { onNavigate(AppScreen.EXPLORE) }) {
                        Text(
                            text = "See All",
                            color = MysoreGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(attractions.take(8)) { attraction ->
                        AttractionHighlightCard(
                            attraction = attraction,
                            onClick = { onAttractionClick(attraction) }
                        )
                    }
                }
            }
        }

        // 4. INTERACTIVE 3D MAP SECTION
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                InteractiveMysoreMap(
                    markers = markers,
                    onMarkerSelected = { marker ->
                        val matchingAttraction = attractions.find { it.id == marker.id }
                        if (matchingAttraction != null) {
                            onAttractionClick(matchingAttraction)
                        } else {
                            onNavigate(AppScreen.MAP)
                        }
                    }
                )
            }
        }

        // 5. AI TRIP PLANNER BANNER / TEASER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(RoyalPurpleContainer, Color(0xFF1E1038), Color(0xFF28184C))
                        )
                    )
                    .border(1.5.dp, GlassBorderGold, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column {
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
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "AI TRIP PLANNER",
                            color = MysoreGoldLight,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Build Your Perfect Mysore Trip",
                        style = MaterialTheme.typography.headlineLarge,
                        color = WarmIvory,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Customize by length of stay, heritage, royal food trails, photography, and nature. Get optimized time-slots and local tips.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmIvoryMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onNavigate(AppScreen.PLANNER) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MysoreGold,
                            contentColor = MidnightNavy
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("home_generate_itinerary_btn")
                    ) {
                        Text(
                            text = "Generate Itinerary",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // 6. FOOD SECTION PREVIEW ("Taste Mysore")
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LOCAL GASTRONOMY",
                            style = MaterialTheme.typography.labelSmall,
                            color = MysoreGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Taste Mysore",
                            style = MaterialTheme.typography.headlineLarge,
                            color = WarmIvory,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    TextButton(onClick = { onNavigate(AppScreen.FOOD) }) {
                        Text(
                            text = "All Dishes",
                            color = MysoreGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(foods) { food ->
                        FoodMiniCard(
                            food = food,
                            onClick = { onNavigate(AppScreen.FOOD) }
                        )
                    }
                }
            }
        }

        // 7. EXPERIENCE SECTION ("Experience Mysore Like a Local")
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "UNFORGETTABLE ADVENTURES",
                    style = MaterialTheme.typography.labelSmall,
                    color = MysoreGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Experience Mysore Like a Local",
                    style = MaterialTheme.typography.headlineLarge,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(14.dp))

                experiences.take(4).forEach { exp ->
                    ExperienceCard(
                        experience = exp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }

        // 8. WEATHER WIDGET
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                WeatherCard(weather = weather)
            }
        }

        // 9. TRAVEL INFORMATION DASHBOARD
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "TRAVEL GUIDE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MysoreGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Practical Information",
                    style = MaterialTheme.typography.headlineLarge,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(14.dp))

                TravelInfoDashboard(travelInfo = travelInfo)
            }
        }

        // 10. PREMIUM FOOTER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF060913))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👑  MYSORE EXPLORER",
                    style = MaterialTheme.typography.titleMedium,
                    color = MysoreGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Your digital guide to discovering the City of Palaces.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmIvoryMuted
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Explore",
                        color = MysoreGoldLight,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(AppScreen.EXPLORE) }
                    )
                    Text(
                        text = "Itineraries",
                        color = MysoreGoldLight,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(AppScreen.PLANNER) }
                    )
                    Text(
                        text = "Food",
                        color = MysoreGoldLight,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(AppScreen.FOOD) }
                    )
                    Text(
                        text = "Map",
                        color = MysoreGoldLight,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onNavigate(AppScreen.MAP) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Made with ❤️ for curious travelers.",
                    color = WarmIvoryMuted.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = category.iconEmoji,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.displayName,
                color = WarmIvory,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.description,
                color = WarmIvoryMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AttractionHighlightCard(
    attraction: Attraction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(260.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                AsyncImage(
                    model = attraction.imageUrl,
                    contentDescription = attraction.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category pill
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${attraction.category.iconEmoji} ${attraction.category.displayName}",
                        color = MysoreGoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Rating pill
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "★ ${attraction.rating}",
                        color = MysoreGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = attraction.name,
                    color = WarmIvory,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = attraction.location,
                    color = WarmIvoryMuted,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⏱ ${attraction.durationText}",
                        color = MysoreGoldLight,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Explore →",
                        color = MysoreGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun FoodMiniCard(
    food: FoodItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .border(1.dp, GlassBorderGold, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = food.tag,
                        color = MysoreGold,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = food.name,
                    color = WarmIvory,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = food.priceRange,
                    color = MysoreGoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.famousPlaces.firstOrNull() ?: "",
                    color = WarmIvoryMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ExperienceCard(
    experience: ExperienceItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GlassBorderGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = experience.imageUrl,
                contentDescription = experience.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = experience.category.uppercase(),
                        color = MysoreGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = experience.duration,
                        color = WarmIvoryMuted,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = experience.title,
                    color = WarmIvory,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = experience.description,
                    color = WarmIvoryMuted,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TravelInfoDashboard(
    travelInfo: TravelInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Best time card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MysoreGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BEST TIME TO VISIT",
                        color = MysoreGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = travelInfo.bestSeason,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = travelInfo.bestMonths,
                    color = WarmIvoryMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Getting Around options
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = MysoreGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GETTING AROUND",
                        color = MysoreGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                travelInfo.gettingAround.forEach { opt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = opt.mode,
                                color = WarmIvory,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = opt.tip,
                                color = WarmIvoryMuted,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = opt.priceGuide,
                            color = MysoreGoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (opt != travelInfo.gettingAround.last()) {
                        HorizontalDivider(
                            color = Color(0x1AFFFFFF),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Popular Day Trips
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = null,
                        tint = MysoreGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "POPULAR DAY TRIPS",
                        color = MysoreGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                travelInfo.dayTrips.forEach { trip ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${trip.destination} (${trip.distanceKm} km • ${trip.travelTime})",
                                color = WarmIvory,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = trip.highlights,
                                color = WarmIvoryMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
