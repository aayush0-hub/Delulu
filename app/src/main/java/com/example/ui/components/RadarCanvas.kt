package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.IntentMode
import com.example.data.model.Profile
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.VioletSecondary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun RadarCanvas(
    profiles: List<Profile>,
    maxRadiusMeters: Int,
    isScanning: Boolean,
    onBlipClick: (Profile) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_anim")

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_angle"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("radar_canvas")
                .pointerInput(profiles, maxRadiusMeters) {
                    detectTapGestures { tapOffset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val maxDrawingRadius = size.width * 0.44f

                        // Find closest profile to tap
                        for (p in profiles) {
                            val ratio = (p.distanceMeters.toFloat() / maxRadiusMeters.toFloat()).coerceIn(0.08f, 0.98f)
                            val r = ratio * maxDrawingRadius
                            val rad = Math.toRadians(p.angleDegrees.toDouble())
                            val bx = center.x + (r * cos(rad)).toFloat()
                            val by = center.y + (r * sin(rad)).toFloat()

                            val distanceSq = (tapOffset.x - bx) * (tapOffset.x - bx) + (tapOffset.y - by) * (tapOffset.y - by)
                            // 36dp touch radius tolerance
                            if (distanceSq <= 40 * 40 * density) {
                                onBlipClick(p)
                                break
                            }
                        }
                    }
                }
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxDrawingRadius = size.width * 0.44f

            // Draw concentric range rings (500m, 1000m, 1500m, 2000m)
            val ringDistances = listOf(0.25f, 0.50f, 0.75f, 1.0f)
            val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

            ringDistances.forEachIndexed { index, fraction ->
                val ringRadius = maxDrawingRadius * fraction
                drawCircle(
                    color = if (fraction == 1.0f) CoralPrimary.copy(alpha = 0.4f) else VioletSecondary.copy(alpha = 0.25f),
                    radius = ringRadius,
                    center = center,
                    style = Stroke(
                        width = if (fraction == 1.0f) 2f else 1f,
                        pathEffect = if (fraction < 1.0f) dashedEffect else null
                    )
                )

                // Label on ring
                val labelMeters = (maxRadiusMeters * fraction).toInt()
                val labelText = if (labelMeters >= 1000) "${labelMeters / 1000}km" else "${labelMeters}m"
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(120, 160, 150, 200)
                        textSize = 26f
                        isAntiAlias = true
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText(labelText, center.x, center.y - ringRadius + 14f, paint)
                }
            }

            // Crosshair axes
            drawLine(
                color = VioletSecondary.copy(alpha = 0.2f),
                start = Offset(center.x, center.y - maxDrawingRadius),
                end = Offset(center.x, center.y + maxDrawingRadius),
                strokeWidth = 1f
            )
            drawLine(
                color = VioletSecondary.copy(alpha = 0.2f),
                start = Offset(center.x - maxDrawingRadius, center.y),
                end = Offset(center.x + maxDrawingRadius, center.y),
                strokeWidth = 1f
            )

            // Scanning radar beam sweep
            if (isScanning) {
                val sweepRad = Math.toRadians(sweepAngle.toDouble())
                val beamEnd = Offset(
                    (center.x + maxDrawingRadius * cos(sweepRad)).toFloat(),
                    (center.y + maxDrawingRadius * sin(sweepRad)).toFloat()
                )

                // Sweep line
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(CoralPrimary.copy(alpha = 0.1f), CoralPrimary.copy(alpha = 0.85f)),
                        start = center,
                        end = beamEnd
                    ),
                    start = center,
                    end = beamEnd,
                    strokeWidth = 2.5f
                )

                // Sweep light wedge arc
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.Transparent,
                            CoralPrimary.copy(alpha = 0.03f),
                            CoralPrimary.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        center = center
                    ),
                    startAngle = sweepAngle - 45f,
                    sweepAngle = 45f,
                    useCenter = true,
                    topLeft = Offset(center.x - maxDrawingRadius, center.y - maxDrawingRadius),
                    size = androidx.compose.ui.geometry.Size(maxDrawingRadius * 2, maxDrawingRadius * 2)
                )
            }

            // Draw center user blip
            drawCircle(
                color = CyanTertiary.copy(alpha = 0.25f),
                radius = 16f * pulseScale,
                center = center
            )
            drawCircle(
                color = CyanTertiary,
                radius = 7f,
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = 3f,
                center = center
            )

            // Draw nearby profile blips
            profiles.forEach { p ->
                val ratio = (p.distanceMeters.toFloat() / maxRadiusMeters.toFloat()).coerceIn(0.08f, 0.98f)
                val r = ratio * maxDrawingRadius
                val rad = Math.toRadians(p.angleDegrees.toDouble())
                val bx = (center.x + r * cos(rad)).toFloat()
                val by = (center.y + r * sin(rad)).toFloat()
                val blipPos = Offset(bx, by)

                val blipColor = when (p.mode) {
                    IntentMode.DATING -> CoralPrimary
                    IntentMode.FRIENDSHIP -> CyanTertiary
                    IntentMode.BOTH -> VioletSecondary
                }

                // Check angle difference with sweep beam to pulse nearby blips
                val angleDiff = Math.abs((p.angleDegrees - sweepAngle + 360) % 360)
                val isRecentlyScanned = isScanning && angleDiff < 35f

                if (isRecentlyScanned) {
                    drawCircle(
                        color = blipColor.copy(alpha = 0.45f),
                        radius = 20f,
                        center = blipPos
                    )
                }

                // Outer halo
                drawCircle(
                    color = blipColor.copy(alpha = 0.35f),
                    radius = 12f,
                    center = blipPos
                )
                // Core dot
                drawCircle(
                    color = blipColor,
                    radius = 6.5f,
                    center = blipPos
                )
                // Inner highlight
                drawCircle(
                    color = Color.White,
                    radius = 2.5f,
                    center = blipPos
                )

                // Emoji Label next to blip
                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = android.graphics.Paint().apply {
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText(p.avatarEmoji, bx, by - 12f, textPaint)
                }
            }
        }
    }
}
