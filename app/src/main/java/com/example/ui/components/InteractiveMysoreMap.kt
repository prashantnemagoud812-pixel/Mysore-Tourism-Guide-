package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Category
import com.example.data.model.MysoreMapMarker
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun InteractiveMysoreMap(
    markers: List<MysoreMapMarker>,
    onMarkerSelected: (MysoreMapMarker) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedMarker by remember { mutableStateOf<MysoreMapMarker?>(markers.firstOrNull()) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }
    var isNearbyPulseActive by remember { mutableStateOf(false) }
    var userLocationActive by remember { mutableStateOf(false) }

    // Pulsing ripple for active landmark marker
    val pulseTransition = rememberInfiniteTransition(label = "map_pulse")
    val pulseRadius by pulseTransition.animateFloat(
        initialValue = 6f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )
    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseOutQuad),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    val filteredMarkers = remember(selectedCategory, markers) {
        if (selectedCategory == null) markers else markers.filter { it.category == selectedCategory }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, GlassBorderGold, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EXPLORE ON 3D MAP",
                        style = MaterialTheme.typography.labelSmall,
                        color = MysoreGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Interactive Mysore Guide",
                        style = MaterialTheme.typography.titleLarge,
                        color = WarmIvory,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Category badges
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = {
                            userLocationActive = !userLocationActive
                            if (userLocationActive) {
                                panOffsetX = 0f
                                panOffsetY = 0f
                            }
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (userLocationActive) MysoreGold else Color(0x33FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location",
                            tint = if (userLocationActive) MidnightNavy else WarmIvory,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { isNearbyPulseActive = !isNearbyPulseActive },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isNearbyPulseActive) RoyalPurple else Color(0x33FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radar,
                            contentDescription = "Radar Nearby",
                            tint = WarmIvory,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filterItems = listOf(
                    null to "All",
                    Category.HERITAGE to "Attractions",
                    Category.FOOD to "Food",
                    Category.STAY to "Hotels",
                    Category.SHOPPING to "Shopping"
                )
                filterItems.forEach { (cat, label) ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MysoreGold else Color(0x22FFFFFF))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) MidnightNavy else WarmIvoryMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stylized 3D Map Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0C1124))
                    .border(1.dp, Color(0x33FFD700), RoundedCornerShape(18.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            panOffsetX = (panOffsetX + dragAmount.x).coerceIn(-120f, 120f)
                            panOffsetY = (panOffsetY + dragAmount.y).coerceIn(-120f, 120f)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f + panOffsetX
                    val cy = h / 2f + panOffsetY

                    // 1. Grid lines / Isometric topography
                    val gridStep = 40f
                    for (x in -200..w.toInt() + 200 step gridStep.toInt()) {
                        drawLine(
                            color = Color(0x153A4770),
                            start = Offset(x.toFloat() + panOffsetX * 0.3f, 0f),
                            end = Offset(x.toFloat() + panOffsetX * 0.3f, h),
                            strokeWidth = 1f
                        )
                    }
                    for (y in -200..h.toInt() + 200 step gridStep.toInt()) {
                        drawLine(
                            color = Color(0x153A4770),
                            start = Offset(0f, y.toFloat() + panOffsetY * 0.3f),
                            end = Offset(w, y.toFloat() + panOffsetY * 0.3f),
                            strokeWidth = 1f
                        )
                    }

                    // 2. Cauvery River Channel flowing at North (Brindavan & Srirangapatna)
                    val riverPath = Path().apply {
                        moveTo(0f, cy - 85f)
                        cubicTo(w * 0.3f, cy - 100f, w * 0.6f, cy - 70f, w, cy - 95f)
                    }
                    drawPath(
                        path = riverPath,
                        color = Color(0xFF1E5F8A).copy(alpha = 0.5f),
                        style = Stroke(width = 14f)
                    )

                    // 3. Chamundi Hill Contour (South East elevation)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x3355442A), Color.Transparent),
                            center = Offset(cx + 80f, cy + 70f),
                            radius = 90f
                        ),
                        radius = 90f,
                        center = Offset(cx + 80f, cy + 70f)
                    )
                    drawCircle(
                        color = Color(0x44D4AF37),
                        radius = 55f,
                        center = Offset(cx + 80f, cy + 70f),
                        style = Stroke(width = 1.5f)
                    )

                    // 4. Outer Ring Road Loop
                    drawOval(
                        color = Color(0x33FFD700),
                        topLeft = Offset(cx - 130f, cy - 110f),
                        size = Size(260f, 220f),
                        style = Stroke(width = 2f)
                    )

                    // 5. User Location Marker (Simulated GPS / Real GPS)
                    if (userLocationActive) {
                        drawCircle(
                            color = Color(0xFF38BDF8).copy(alpha = 0.3f),
                            radius = 28f,
                            center = Offset(cx - 20f, cy + 10f)
                        )
                        drawCircle(
                            color = Color(0xFF38BDF8),
                            radius = 8f,
                            center = Offset(cx - 20f, cy + 10f)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.5f,
                            center = Offset(cx - 20f, cy + 10f)
                        )
                    }

                    // 6. Sonar Nearby Radar Pulse
                    if (isNearbyPulseActive) {
                        drawCircle(
                            color = RoyalPurpleLight.copy(alpha = pulseAlpha * 0.5f),
                            radius = pulseRadius * 4f,
                            center = Offset(cx, cy)
                        )
                    }
                }

                // Render Landmark Markers positioned relative to center
                val centerLat = 12.3051
                val centerLng = 76.6551
                val latScale = 1400.0
                val lngScale = 1400.0

                filteredMarkers.forEach { marker ->
                    val isSelected = selectedMarker?.id == marker.id
                    val markerX = (140f + (marker.longitude - centerLng) * lngScale + panOffsetX).toFloat()
                    val markerY = (140f - (marker.latitude - centerLat) * latScale + panOffsetY).toFloat()

                    Box(
                        modifier = Modifier
                            .offset(x = (markerX - 18).dp, y = (markerY - 18).dp)
                            .clickable {
                                selectedMarker = marker
                                onMarkerSelected(marker)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MysoreGold.copy(alpha = pulseAlpha))
                            )
                        }

                        // Marker Pin
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 32.dp else 26.dp)
                                .clip(CircleShape)
                                .background(
                                    when (marker.category) {
                                        Category.HERITAGE -> MysoreGold
                                        Category.FOOD -> Color(0xFFF97316)
                                        Category.NATURE -> EmeraldGreen
                                        Category.SHOPPING -> RoyalPurpleLight
                                        Category.STAY -> Color(0xFF38BDF8)
                                        Category.CULTURE -> Color(0xFFEC4899)
                                    }
                                )
                                .border(1.5.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = marker.category.iconEmoji,
                                fontSize = if (isSelected) 14.sp else 11.sp
                            )
                        }
                    }
                }

                // Drag hint
                Text(
                    text = "Drag to pan map",
                    color = WarmIvoryMuted.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selected Marker Detail Popup Card (Glassmorphism)
            AnimatedVisibility(visible = selectedMarker != null) {
                selectedMarker?.let { marker ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x38182245))
                            .border(1.dp, GlassBorderGold, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = marker.image,
                            contentDescription = marker.title,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = marker.title,
                                    color = WarmIvory,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "★ ${marker.rating}",
                                    color = MysoreGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${marker.subtitle} • ${marker.category.displayName}",
                                color = WarmIvoryMuted,
                                fontSize = 12.sp
                            )
                        }
                        IconButton(
                            onClick = { onMarkerSelected(marker) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MysoreGold)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "View",
                                tint = MidnightNavy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
