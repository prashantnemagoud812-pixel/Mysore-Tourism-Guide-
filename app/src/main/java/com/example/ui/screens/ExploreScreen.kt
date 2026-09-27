package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Attraction
import com.example.data.model.Category
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: MysoreViewModel,
    onAttractionClick: (Attraction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val uiState by viewModel.uiState.collectAsState()
    val searchQuery = uiState.searchQuery
    val selectedCategory = uiState.selectedCategory
    val bookmarks = uiState.bookmarks

    val filteredAttractions = remember(searchQuery, selectedCategory, viewModel.attractions) {
        viewModel.attractions.filter { item ->
            val matchesCategory = selectedCategory == null || item.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true) ||
                    item.location.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavy)
    ) {
        // Top App Bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Explore Mysore",
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

        // Search Bar & Suggestions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search palaces, dosa, silk, temples...",
                        color = WarmIvoryMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MysoreGold
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = WarmIvoryMuted
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("explore_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MidnightNavyCard,
                    unfocusedContainerColor = MidnightNavyCard,
                    focusedBorderColor = MysoreGold,
                    unfocusedBorderColor = GlassBorderGold,
                    focusedTextColor = WarmIvory,
                    unfocusedTextColor = WarmIvory
                ),
                singleLine = true
            )

            // Quick suggestion chips
            if (searchQuery.isEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(viewModel.searchSuggestions) { suggestion ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x26FFD700))
                                .border(0.5.dp, GlassBorderGold, RoundedCornerShape(12.dp))
                                .clickable { viewModel.updateSearchQuery(suggestion) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = suggestion,
                                color = MysoreGoldLight,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.selectCategory(null) },
                    label = { Text("All", fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MysoreGold,
                        selectedLabelColor = MidnightNavy,
                        containerColor = MidnightNavyCard,
                        labelColor = WarmIvoryMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = GlassBorderGold,
                        enabled = true,
                        selected = selectedCategory == null
                    )
                )
            }
            items(Category.values()) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectCategory(if (isSelected) null else cat) },
                    label = { Text("${cat.iconEmoji} ${cat.displayName}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MysoreGold,
                        selectedLabelColor = MidnightNavy,
                        containerColor = MidnightNavyCard,
                        labelColor = WarmIvoryMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = GlassBorderGold,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Result count
        Text(
            text = "${filteredAttractions.size} Destinations Found",
            color = WarmIvoryMuted,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Attractions List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredAttractions) { attraction ->
                val isBookmarked = bookmarks.any { it.id == attraction.id }
                AttractionCard(
                    attraction = attraction,
                    isBookmarked = isBookmarked,
                    onBookmarkToggle = { viewModel.toggleBookmark(attraction) },
                    onClick = { onAttractionClick(attraction) }
                )
            }
        }
    }
}

@Composable
fun AttractionCard(
    attraction: Attraction,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = attraction.imageUrl,
                    contentDescription = attraction.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category tag
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${attraction.category.iconEmoji} ${attraction.category.displayName}",
                        color = MysoreGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bookmark Icon Button
                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xD90A0E1F))
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (isBookmarked) MysoreGold else WarmIvory
                    )
                }

                // Duration & Rating badge at bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★ ${attraction.rating} (${attraction.reviewCount.div(1000)}k+ reviews)",
                        color = MysoreGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "⏱ ${attraction.durationText}",
                        color = WarmIvoryMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = attraction.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = WarmIvory,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = attraction.tagLine,
                    style = MaterialTheme.typography.bodySmall,
                    color = MysoreGoldLight,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = attraction.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmIvoryMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MysoreGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${attraction.distanceKm} km from center",
                            color = WarmIvoryMuted,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MysoreGold,
                            contentColor = MidnightNavy
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Explore",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
