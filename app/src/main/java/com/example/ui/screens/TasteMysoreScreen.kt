package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.FoodItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasteMysoreScreen(
    viewModel: MysoreViewModel,
    onBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val foods = viewModel.foods

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MidnightNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Taste Mysore",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "ROYAL KITCHENS & STREET CLASSICS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MysoreGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Iconic Culinary Heritage",
                        style = MaterialTheme.typography.headlineLarge,
                        color = WarmIvory,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "From century-old royal recipes created for the Wadiyars to bustling Agrahara tiffin rooms, explore Mysore's most beloved dishes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmIvoryMuted
                    )
                }
            }

            items(foods) { food ->
                FoodDetailCard(
                    food = food,
                    onFindNearby = { onNavigate(AppScreen.MAP) }
                )
            }
        }
    }
}

@Composable
fun FoodDetailCard(
    food: FoodItem,
    onFindNearby: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Tag
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = food.tag,
                        color = MysoreGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Price badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD90A0E1F))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = food.priceRange,
                        color = WarmIvory,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = WarmIvory,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = food.kannadaName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MysoreGoldLight
                        )
                    }

                    if (food.isVegetarian) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .border(1.5.dp, EmeraldGreen, RoundedCornerShape(4.dp))
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = food.fullStory,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmIvoryMuted,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Where to Try
                Text(
                    text = "Authentic Places to Try:",
                    color = MysoreGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                food.famousPlaces.forEach { place ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "• ", color = MysoreGoldLight, fontSize = 12.sp)
                        Text(
                            text = place,
                            color = WarmIvory,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onFindNearby,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MysoreGold),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MysoreGold),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Find Nearby Eateries",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
