package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IntentMode
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.VioletSecondary

@Composable
fun VerifiedBadge(
    isVerified: Boolean,
    verificationMethod: String = "",
    photoMatchScore: Int = 0,
    compact: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    if (!isVerified) return

    val label = when {
        verificationMethod.contains("Selfie", ignoreCase = true) && photoMatchScore > 0 -> "Selfie $photoMatchScore%"
        verificationMethod.contains("Selfie", ignoreCase = true) -> "Selfie Match"
        verificationMethod.contains("Phone", ignoreCase = true) -> "Phone Confirmed"
        else -> "Verified"
    }

    if (compact) {
        Surface(
            shape = CircleShape,
            color = CyanTertiary.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyanTertiary.copy(alpha = 0.6f)),
            modifier = modifier
                .clip(CircleShape)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .testTag("badge_verified_compact")
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(CyanTertiary, VioletSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified Profile",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    } else {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = CyanTertiary.copy(alpha = 0.12f),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyanTertiary.copy(alpha = 0.4f)),
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .testTag("badge_verified_full")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = CyanTertiary,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanTertiary
                )
            }
        }
    }
}

@Composable
fun AvatarBadge(
    avatarEmoji: String,
    avatarColor: Long,
    size: Dp = 56.dp,
    showOnlineDot: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Gradient Ring Border
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    Brush.sweepGradient(
                        listOf(
                            Color(avatarColor),
                            CoralPrimary,
                            VioletSecondary,
                            CyanTertiary,
                            Color(avatarColor)
                        )
                    )
                )
                .padding(2.5.dp)
                .clip(CircleShape)
                .background(Color(avatarColor).copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = avatarEmoji,
                fontSize = (size.value * 0.45f).sp,
                textAlign = TextAlign.Center
            )
        }

        if (showOnlineDot) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(EmeraldGreen)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}

@Composable
fun DistanceBadge(
    distanceMeters: Int,
    modifier: Modifier = Modifier
) {
    val (label, bgTint, textTint) = when {
        distanceMeters < 500 -> Triple(
            "${distanceMeters}m away",
            EmeraldGreen.copy(alpha = 0.15f),
            EmeraldGreen
        )
        distanceMeters < 1000 -> Triple(
            "${distanceMeters}m away",
            CyanTertiary.copy(alpha = 0.15f),
            CyanTertiary
        )
        else -> Triple(
            String.format("%.1f km away", distanceMeters / 1000.0),
            VioletSecondary.copy(alpha = 0.15f),
            VioletSecondary
        )
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgTint,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.NearMe,
                contentDescription = null,
                tint = textTint,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textTint
            )
        }
    }
}

@Composable
fun ModeTag(
    mode: IntentMode,
    modifier: Modifier = Modifier
) {
    val (text, icon, color) = when (mode) {
        IntentMode.DATING -> Triple("Dating", Icons.Default.Favorite, CoralPrimary)
        IntentMode.FRIENDSHIP -> Triple("Friends", Icons.Default.Group, CyanTertiary)
        IntentMode.BOTH -> Triple("Dating & Friends", Icons.Default.AutoAwesome, VioletSecondary)
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.18f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagChips(
    tags: List<String>,
    modifier: Modifier = Modifier,
    maxTags: Int = 4
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {
        tags.take(maxTags).forEach { tag ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = tag,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ModeFilterBar(
    selectedMode: IntentMode?,
    onSelectMode: (IntentMode?) -> Unit,
    verifiedOnly: Boolean = false,
    onToggleVerifiedOnly: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = selectedMode == null,
            onClick = { onSelectMode(null) },
            label = { Text("All", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.NearMe,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedMode == null,
                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            modifier = Modifier.testTag("filter_all")
        )

        FilterChip(
            selected = selectedMode == IntentMode.DATING,
            onClick = { onSelectMode(IntentMode.DATING) },
            label = { Text("❤️ Dating", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CoralPrimary.copy(alpha = 0.25f),
                selectedLabelColor = CoralPrimary,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedMode == IntentMode.DATING,
                borderColor = if (selectedMode == IntentMode.DATING) CoralPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            modifier = Modifier.testTag("filter_dating")
        )

        FilterChip(
            selected = selectedMode == IntentMode.FRIENDSHIP,
            onClick = { onSelectMode(IntentMode.FRIENDSHIP) },
            label = { Text("🤝 Friends", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = CyanTertiary.copy(alpha = 0.25f),
                selectedLabelColor = CyanTertiary,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selectedMode == IntentMode.FRIENDSHIP,
                borderColor = if (selectedMode == IntentMode.FRIENDSHIP) CyanTertiary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            modifier = Modifier.testTag("filter_friendship")
        )

        if (onToggleVerifiedOnly != null) {
            FilterChip(
                selected = verifiedOnly,
                onClick = onToggleVerifiedOnly,
                label = { Text("🛡️ Verified", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CyanTertiary.copy(alpha = 0.3f),
                    selectedLabelColor = CyanTertiary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = verifiedOnly,
                    borderColor = if (verifiedOnly) CyanTertiary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                modifier = Modifier.testTag("filter_verified_only")
            )
        }
    }
}

@Composable
fun DistanceRadiusSelector(
    currentRadiusMeters: Int,
    onRadiusChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        tint = CyanTertiary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Radar Proximity Limit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CoralPrimary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = String.format("%.1f km max", currentRadiusMeters / 1000.0),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Slider(
                value = currentRadiusMeters.toFloat(),
                onValueChange = { onRadiusChange(it.toInt()) },
                valueRange = 300f..2000f,
                steps = 16,
                colors = SliderDefaults.colors(
                    thumbColor = CoralPrimary,
                    activeTrackColor = CoralPrimary,
                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                modifier = Modifier.testTag("radius_slider")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(500, 1000, 1500, 2000).forEach { dist ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (currentRadiusMeters == dist) CyanTertiary.copy(alpha = 0.2f) else Color.Transparent,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onRadiusChange(dist) }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (dist == 2000) "2.0 km" else "${dist}m",
                            fontSize = 11.sp,
                            fontWeight = if (currentRadiusMeters == dist) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentRadiusMeters == dist) CyanTertiary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
