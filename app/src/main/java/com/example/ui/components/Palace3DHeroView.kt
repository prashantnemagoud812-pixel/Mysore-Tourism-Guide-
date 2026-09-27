package com.example.ui.components

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.*

enum class LightingMode(val label: String, val icon: String) {
    MORNING("Dawn Glow", "🌅"),
    SUNSET("Golden Sunset", "🌇"),
    ROYAL_NIGHT("Palace Illumination", "✨")
}

@Composable
fun Palace3DHeroView(
    onExploreClick: () -> Unit,
    onPlanTripClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var lightingMode by remember { mutableStateOf(LightingMode.ROYAL_NIGHT) }
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    // Smooth ambient floating motion
    val infiniteTransition = rememberInfiniteTransition(label = "hero_anim")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val sparklePulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                Brush.verticalGradient(
                    colors = when (lightingMode) {
                        LightingMode.MORNING -> listOf(
                            Color(0xFF131A33),
                            Color(0xFF281E40),
                            Color(0xFF45244A)
                        )
                        LightingMode.SUNSET -> listOf(
                            Color(0xFF10142A),
                            Color(0xFF38153A),
                            Color(0xFF5A2530)
                        )
                        LightingMode.ROYAL_NIGHT -> listOf(
                            Color(0xFF070A14),
                            Color(0xFF0E1328),
                            Color(0xFF1A1238)
                        )
                    }
                )
            )
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Royal Subtitle Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x33FFD700))
                    .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👑  EXPLORE • EXPERIENCE • REMEMBER",
                    color = MysoreGoldLight,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Headline
            Text(
                text = "Discover the Royal Soul of Mysore",
                style = MaterialTheme.typography.displayMedium,
                color = WarmIvory,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Explore palaces, heritage, food, culture, and unforgettable experiences in the City of Palaces.",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmIvoryMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3D Interactive Mysore Palace Stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x280D142C))
                    .border(1.dp, GlassBorderGold.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                rotationX = 0f
                                rotationY = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                rotationY = (rotationY + dragAmount.x * 0.15f).coerceIn(-25f, 25f)
                                rotationX = (rotationX - dragAmount.y * 0.15f).coerceIn(-15f, 15f)
                            }
                        )
                    }
                    .graphicsLayer {
                        this.rotationX = rotationX
                        this.rotationY = rotationY
                        this.translationY = floatAnim
                        this.cameraDistance = 16f * density
                    },
                contentAlignment = Alignment.Center
            ) {
                // Procedural 3D Palace & Illumination Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawPalace3DScene(lightingMode, sparklePulse, rotationY)
                }

                // Interactive 3D Rotation Hint
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x990A0E1F))
                        .border(1.dp, Color(0x33FFD700), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateRight,
                        contentDescription = "3D Interactive View",
                        tint = MysoreGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Touch & Drag 3D",
                        color = MysoreGoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Interactive Lighting Selector (Morning, Sunset, Royal Night)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xD90A0E1F))
                        .border(1.dp, GlassBorderGold, RoundedCornerShape(20.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LightingMode.values().forEach { mode ->
                        val isSelected = lightingMode == mode
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) MysoreGold else Color.Transparent)
                                .clickable { lightingMode = mode }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${mode.icon} ${mode.label}",
                                color = if (isSelected) MidnightNavy else WarmIvoryMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Floating "Today in Mysore" Weather & Visiting Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x40182245))
                    .border(1.dp, GlassBorderGold.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFD700)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Weather",
                        tint = MysoreGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Today in Mysore • 27°C",
                            color = WarmIvory,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x3310B981))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Perfect Day",
                                color = EmeraldGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Pleasant breeze for palace gardens; sunset at 6:18 PM on Chamundi Hill.",
                        color = WarmIvoryMuted,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Primary CTA Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onExploreClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("explore_mysore_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MysoreGold,
                        contentColor = MidnightNavy
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Explore Mysore",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = onPlanTripClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("plan_my_trip_button"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MysoreGoldLight
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MysoreGold),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Build My Trip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Scroll indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MysoreGold.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "SCROLL TO EXPLORE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MysoreGold.copy(alpha = 0.8f),
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// Procedural Canvas drawing for 3D Mysore Palace Architectural Facade & Ambient Illumination
private fun DrawScope.drawPalace3DScene(
    mode: LightingMode,
    sparklePulse: Float,
    tiltOffset: Float
) {
    val width = size.width
    val height = size.height
    val centerX = width / 2f + tiltOffset * 1.5f
    val baseY = height - 55f

    // 1. Sky & Ambient Glow
    val ambientGlowColor = when (mode) {
        LightingMode.MORNING -> Color(0x66FF8A65)
        LightingMode.SUNSET -> Color(0x88FF7043)
        LightingMode.ROYAL_NIGHT -> Color(0x99FFA000)
    }

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(ambientGlowColor, Color.Transparent),
            center = Offset(centerX, baseY - 60f),
            radius = width * 0.45f
        ),
        radius = width * 0.45f,
        center = Offset(centerX, baseY - 60f)
    )

    // 2. Stars / Floating Golden Particles in the Night Sky
    if (mode == LightingMode.ROYAL_NIGHT) {
        val starPositions = listOf(
            Offset(width * 0.15f, height * 0.20f),
            Offset(width * 0.28f, height * 0.12f),
            Offset(width * 0.78f, height * 0.16f),
            Offset(width * 0.88f, height * 0.28f),
            Offset(width * 0.50f, height * 0.08f),
            Offset(width * 0.65f, height * 0.22f)
        )
        starPositions.forEachIndexed { index, pos ->
            val factor = ((sin((index + sparklePulse) * 2.0) + 1.0) / 2.0).toFloat()
            drawCircle(
                color = MysoreGoldLight.copy(alpha = 0.3f + 0.7f * factor),
                radius = 1.8f * factor + 1.2f,
                center = pos
            )
        }
    }

    // 3. Chamundi Hill Silhouette in the distant background
    val hillPath = Path().apply {
        moveTo(0f, baseY)
        cubicTo(
            width * 0.2f, baseY - 35f,
            width * 0.35f, baseY - 60f,
            width * 0.45f, baseY - 40f
        )
        cubicTo(
            width * 0.55f, baseY - 20f,
            width * 0.75f, baseY - 50f,
            width, baseY
        )
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }
    drawPath(
        path = hillPath,
        color = Color(0x33090C1A)
    )

    // 4. Ground Promenade with Royal Lawn & Reflection
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF141930), Color(0xFF0A0D1B)),
            startY = baseY,
            endY = height
        ),
        topLeft = Offset(0f, baseY),
        size = Size(width, height - baseY)
    )

    // 5. Palace Main Grand Structure
    val palaceGold = if (mode == LightingMode.ROYAL_NIGHT) Color(0xFFFFD54F) else Color(0xFFE0C475)
    val palaceShadow = Color(0xFF151930)
    val palaceAccent = Color(0xFFFFA000)

    val palaceW = min(width * 0.75f, 320f)
    val palaceLeft = centerX - palaceW / 2f
    val palaceTop = baseY - 85f

    // Main base building block
    drawRoundRect(
        color = palaceShadow,
        topLeft = Offset(palaceLeft, palaceTop + 30f),
        size = Size(palaceW, 55f),
        cornerRadius = CornerRadius(4f, 4f)
    )

    // Arches along lower facade
    val archCount = 7
    val archWidth = palaceW / (archCount + 1)
    for (i in 1..archCount) {
        val archX = palaceLeft + i * archWidth
        drawArc(
            color = palaceGold.copy(alpha = if (mode == LightingMode.ROYAL_NIGHT) 0.9f else 0.5f),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(archX - 8f, palaceTop + 50f),
            size = Size(16f, 24f),
            style = Stroke(width = 2f)
        )
    }

    // Upper balustrade line
    drawLine(
        color = palaceGold,
        start = Offset(palaceLeft, palaceTop + 30f),
        end = Offset(palaceLeft + palaceW, palaceTop + 30f),
        strokeWidth = 2.5f
    )

    // Center Grand Tower & Royal Durbar Arch
    val centerTowerW = 60f
    val centerTowerH = 50f
    val centerTowerX = centerX - centerTowerW / 2f
    val centerTowerY = palaceTop - 15f

    drawRect(
        color = palaceShadow,
        topLeft = Offset(centerTowerX, centerTowerY),
        size = Size(centerTowerW, centerTowerH)
    )

    // Grand Central Dome (Octagonal Kalasa & Onion Dome)
    val domePath = Path().apply {
        moveTo(centerX - 30f, centerTowerY)
        cubicTo(
            centerX - 35f, centerTowerY - 30f,
            centerX + 35f, centerTowerY - 30f,
            centerX + 30f, centerTowerY
        )
        close()
    }
    drawPath(
        path = domePath,
        color = palaceAccent
    )

    // Golden Kalasa finial tip
    drawLine(
        color = MysoreGoldLight,
        start = Offset(centerX, centerTowerY - 30f),
        end = Offset(centerX, centerTowerY - 48f),
        strokeWidth = 2.5f
    )
    drawCircle(
        color = MysoreGoldLight,
        radius = 3.5f,
        center = Offset(centerX, centerTowerY - 48f)
    )

    // Left and Right Wing Domes
    val wingDomeOffset = palaceW * 0.38f
    listOf(-wingDomeOffset, wingDomeOffset).forEach { offset ->
        val wingX = centerX + offset
        val wingTop = palaceTop + 10f
        drawRect(
            color = palaceShadow,
            topLeft = Offset(wingX - 18f, wingTop),
            size = Size(36f, 25f)
        )
        val smallDome = Path().apply {
            moveTo(wingX - 18f, wingTop)
            cubicTo(
                wingX - 22f, wingTop - 20f,
                wingX + 22f, wingTop - 20f,
                wingX + 18f, wingTop
            )
            close()
        }
        drawPath(smallDome, color = palaceGold)
        // Finials
        drawLine(
            color = MysoreGoldLight,
            start = Offset(wingX, wingTop - 20f),
            end = Offset(wingX, wingTop - 32f),
            strokeWidth = 1.8f
        )
    }

    // 6. Illuminated 100,000 Bulbs Effect (When in Royal Night mode)
    if (mode == LightingMode.ROYAL_NIGHT) {
        val bulbColor = Color(0xFFFFF9C4)
        val bulbGlow = Color(0xFFFFD54F)

        // String of bulbs across the roofline
        val bulbSteps = 24
        for (i in 0..bulbSteps) {
            val bx = palaceLeft + (palaceW / bulbSteps) * i
            val by = palaceTop + 30f
            drawCircle(color = bulbGlow.copy(alpha = 0.5f * sparklePulse), radius = 3.5f, center = Offset(bx, by))
            drawCircle(color = bulbColor, radius = 1.5f, center = Offset(bx, by))
        }

        // Center tower outline bulbs
        val towerBulbs = listOf(
            Offset(centerTowerX, centerTowerY),
            Offset(centerTowerX + centerTowerW, centerTowerY),
            Offset(centerX, centerTowerY - 26f),
            Offset(centerX, centerTowerY - 48f),
            Offset(centerX - 24f, centerTowerY - 14f),
            Offset(centerX + 24f, centerTowerY - 14f)
        )
        towerBulbs.forEach { pos ->
            drawCircle(color = bulbGlow.copy(alpha = 0.6f * sparklePulse), radius = 4f, center = pos)
            drawCircle(color = bulbColor, radius = 2f, center = pos)
        }
    }

    // 7. Foreground Royal Royal Palm Trees
    listOf(palaceLeft - 22f, palaceLeft + palaceW + 22f).forEach { tx ->
        // Trunk
        drawLine(
            color = Color(0xFF1B172E),
            start = Offset(tx, baseY),
            end = Offset(tx, baseY - 45f),
            strokeWidth = 3f
        )
        // Palm fronds
        for (a in -60..60 step 30) {
            val rad = Math.toRadians(a.toDouble() - 90)
            val fx = tx + (22f * cos(rad)).toFloat()
            val fy = (baseY - 45f) + (18f * sin(rad)).toFloat()
            drawLine(
                color = Color(0xFF2C3B30),
                start = Offset(tx, baseY - 45f),
                end = Offset(fx, fy),
                strokeWidth = 2f
            )
        }
    }
}
