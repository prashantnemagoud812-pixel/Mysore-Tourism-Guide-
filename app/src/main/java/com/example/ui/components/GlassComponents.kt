package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeatherInfo
import com.example.ui.theme.*

@Composable
fun RoyalGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val tiltTransition = updateTransition(isPressed, label = "card_tilt")
    val scale by tiltTransition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = "scale"
    ) { if (it) 0.98f else 1f }

    val baseModifier = modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            shadowElevation = 8f
        }
        .clip(RoundedCornerShape(cornerRadius))
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    MidnightNavyCard.copy(alpha = 0.95f),
                    MidnightNavyLight.copy(alpha = 0.90f)
                )
            )
        )
        .border(1.dp, GlassBorderGold, RoundedCornerShape(cornerRadius))

    val finalModifier = if (onClick != null) {
        baseModifier.clickable(onClick = onClick)
    } else {
        baseModifier
    }

    Column(
        modifier = finalModifier.padding(16.dp),
        content = content
    )
}

@Composable
fun WeatherCard(
    weather: WeatherInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MidnightNavyCard)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WEATHER & TIMING",
                        style = MaterialTheme.typography.labelSmall,
                        color = MysoreGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Mysore Today",
                        style = MaterialTheme.typography.titleLarge,
                        color = WarmIvory,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = MysoreGold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${weather.tempC}°C",
                        style = MaterialTheme.typography.headlineLarge,
                        color = WarmIvory,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${weather.condition} • ${weather.travelRecommendation}",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmIvoryMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stats grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x3310162B))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherMetric(
                    label = "Humidity",
                    value = "${weather.humidity}%",
                    icon = Icons.Default.WaterDrop
                )
                WeatherMetric(
                    label = "Wind",
                    value = "${weather.windKmh} km/h",
                    icon = Icons.Default.Air
                )
                WeatherMetric(
                    label = "Sunset",
                    value = weather.sunsetTime,
                    icon = Icons.Default.NightsStay
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Palace Illumination Schedule
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x28FFD700))
                    .border(1.dp, GlassBorderGold, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MysoreGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Palace Illumination Schedule",
                        color = MysoreGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = weather.palaceLightingStatus,
                        color = WarmIvory,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherMetric(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MysoreGoldLight,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = WarmIvory,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = WarmIvoryMuted,
            fontSize = 10.sp
        )
    }
}

@Composable
fun AudioGuidePlayer(
    audioSnippet: String,
    title: String,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }

    // Waveform bar animation
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 4f, targetValue = 24f,
        animationSpec = infiniteRepeatable(tween(400, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "b1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 18f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(550, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "b2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 8f, targetValue = 26f,
        animationSpec = infiniteRepeatable(tween(350, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "b3"
    )
    val bar4 by infiniteTransition.animateFloat(
        initialValue = 22f, targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(600, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "b4"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GlassBorderPurple, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = RoyalPurpleContainer.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MysoreGold)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Audio Guide",
                        tint = MidnightNavy
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🎧 Royal Audio Narration",
                        color = MysoreGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = title,
                        color = WarmIvory,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Waveform animation
                if (isPlaying) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(28.dp)
                    ) {
                        Box(Modifier.width(3.dp).height(bar1.dp).background(MysoreGold, CircleShape))
                        Box(Modifier.width(3.dp).height(bar2.dp).background(MysoreGoldLight, CircleShape))
                        Box(Modifier.width(3.dp).height(bar3.dp).background(MysoreGold, CircleShape))
                        Box(Modifier.width(3.dp).height(bar4.dp).background(MysoreGoldLight, CircleShape))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "“$audioSnippet”",
                color = WarmIvoryMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}
