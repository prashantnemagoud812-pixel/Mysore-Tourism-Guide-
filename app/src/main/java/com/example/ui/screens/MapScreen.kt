package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Attraction
import com.example.ui.components.InteractiveMysoreMap
import com.example.ui.theme.*
import com.example.ui.viewmodel.MysoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MysoreViewModel,
    onAttractionClick: (Attraction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val markers = viewModel.mapMarkers
    val attractions = viewModel.attractions

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MidnightNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mysore 3D Map",
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
                .padding(16.dp)
        ) {
            InteractiveMysoreMap(
                markers = markers,
                onMarkerSelected = { marker ->
                    val matchingAttraction = attractions.find { it.id == marker.id }
                    if (matchingAttraction != null) {
                        onAttractionClick(matchingAttraction)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
