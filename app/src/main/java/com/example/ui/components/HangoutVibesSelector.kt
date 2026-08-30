package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.VioletSecondary

data class VibeCategory(
    val name: String,
    val iconEmoji: String,
    val vibes: List<String>
)

object HangoutVibesCatalog {
    val CLASSICS = listOf(
        "☕ Coffee",
        "🌿 Walk in the park",
        "🎲 Game night"
    )

    val CATEGORIES = listOf(
        VibeCategory(
            name = "All Vibes",
            iconEmoji = "✨",
            vibes = emptyList()
        ),
        VibeCategory(
            name = "Classics",
            iconEmoji = "⭐",
            vibes = listOf("☕ Coffee", "🌿 Walk in the park", "🎲 Game night", "🍕 Late night pizza")
        ),
        VibeCategory(
            name = "Food & Drinks",
            iconEmoji = "☕",
            vibes = listOf(
                "☕ Coffee",
                "🧋 Boba & sweet treats",
                "🍕 Late night pizza",
                "🌮 Street food crawl",
                "🥐 Bakery morning",
                "🍹 Craft drinks & mocktails",
                "🍣 Sushi dinner",
                "🍦 Ice cream stroll"
            )
        ),
        VibeCategory(
            name = "Outdoors & Active",
            iconEmoji = "🌿",
            vibes = listOf(
                "🌿 Walk in the park",
                "🌅 Sunset picnic",
                "🧗 Bouldering & climbing",
                "🐶 Dog park meetup",
                "🚲 Casual bike ride",
                "🏃 Morning jog & 5k",
                "🏸 Pickleball & badminton",
                "🛹 Skatepark sesh"
            )
        ),
        VibeCategory(
            name = "Arts & Culture",
            iconEmoji = "🎨",
            vibes = listOf(
                "🎨 Art gallery crawl",
                "🎵 Live indie music",
                "📚 Book club & reading",
                "🪴 Plant shopping",
                "🍿 Movie & cinema",
                "🛍️ Vintage thrifting",
                "🏺 Pottery & crafts",
                "📸 Photography walk"
            )
        ),
        VibeCategory(
            name = "Social & Chill",
            iconEmoji = "🎲",
            vibes = listOf(
                "🎲 Game night",
                "💻 Co-working & study",
                "🎤 Karaoke session",
                "🧘 Yoga in the park",
                "🧩 Puzzle & chill",
                "🎧 Vinyl listening bar"
            )
        )
    )

    val ALL_DEFAULT_VIBES = listOf(
        "☕ Coffee",
        "🌿 Walk in the park",
        "🎲 Game night",
        "🎨 Art gallery crawl",
        "🧗 Bouldering & climbing",
        "🍕 Late night pizza",
        "🎵 Live indie music",
        "🌅 Sunset picnic",
        "🧋 Boba & sweet treats",
        "📚 Book club & reading",
        "🐶 Dog park meetup",
        "🌮 Street food crawl",
        "🍹 Craft drinks & mocktails",
        "💻 Co-working & study",
        "🍿 Movie & cinema",
        "🚲 Casual bike ride",
        "🪴 Plant shopping",
        "🎤 Karaoke session",
        "🛍️ Vintage thrifting",
        "🥐 Bakery morning",
        "🏸 Pickleball & badminton"
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HangoutVibesSelector(
    selectedVibes: List<String>,
    onVibesChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    maxVibes: Int = 10
) {
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var customVibeInput by remember { mutableStateOf("") }
    var customVibesList by remember { mutableStateOf<List<String>>(emptyList()) }

    val currentCategory = HangoutVibesCatalog.CATEGORIES[selectedCategoryIndex]
    val displayedVibes = remember(selectedCategoryIndex, customVibesList) {
        val baseList = if (selectedCategoryIndex == 0) {
            (HangoutVibesCatalog.ALL_DEFAULT_VIBES + customVibesList + selectedVibes).distinct()
        } else {
            (currentCategory.vibes + customVibesList.filter { it in selectedVibes }).distinct()
        }
        baseList
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Header summary card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
            ),
            border = BorderStroke(1.dp, CoralPrimary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✨", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hangout Vibes & Interests",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CoralPrimary.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, CoralPrimary.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "${selectedVibes.size}/$maxVibes vibes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoralPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select vibes like 'coffee', 'walk in the park', or 'game night' to signal what you're down to do with people within 2km.",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (selectedVibes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Active on your 2km Radar:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanTertiary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        selectedVibes.forEach { vibe ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = VioletSecondary.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, VioletSecondary.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = vibe,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove $vibe",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable {
                                                onVibesChanged(selectedVibes - vibe)
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Preset Action Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Browse Categories",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CoralPrimary.copy(alpha = 0.12f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val newSet = (selectedVibes + HangoutVibesCatalog.CLASSICS).distinct()
                            onVibesChanged(newSet.take(maxVibes))
                        }
                ) {
                    Text(
                        text = "⭐ Add Top 3 Classics",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CoralPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                if (selectedVibes.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onVibesChanged(emptyList()) }
                    ) {
                        Text(
                            text = "Clear",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Filter Chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            HangoutVibesCatalog.CATEGORIES.forEachIndexed { index, cat ->
                val isSelected = selectedCategoryIndex == index
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategoryIndex = index },
                    label = {
                        Text(
                            text = "${cat.iconEmoji} ${cat.name}",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoralPrimary.copy(alpha = 0.25f),
                        selectedLabelColor = CoralPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.testTag("chip_vibe_cat_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Vibe selection pills grid
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("grid_hangout_vibes")
        ) {
            displayedVibes.forEach { vibe ->
                val isSelected = selectedVibes.contains(vibe)
                val animatedBg by animateColorAsState(
                    targetValue = if (isSelected) CoralPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    label = "vibe_bg"
                )
                val animatedBorder by animateColorAsState(
                    targetValue = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    label = "vibe_border"
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = animatedBg,
                    border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, animatedBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val updated = if (isSelected) {
                                selectedVibes - vibe
                            } else {
                                if (selectedVibes.size < maxVibes) selectedVibes + vibe else selectedVibes
                            }
                            onVibesChanged(updated)
                        }
                        .testTag("tag_vibe_${vibe.filter { it.isLetterOrDigit() }}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(CoralPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }

                        Text(
                            text = vibe,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom Vibe Add Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = customVibeInput,
                onValueChange = { customVibeInput = it },
                placeholder = { Text("Add custom vibe (e.g. 🍵 Matcha tasting, 🧘 Yoga)", fontSize = 12.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (customVibeInput.isNotBlank()) {
                        val formatted = customVibeInput.trim()
                        customVibesList = (customVibesList + formatted).distinct()
                        if (!selectedVibes.contains(formatted) && selectedVibes.size < maxVibes) {
                            onVibesChanged(selectedVibes + formatted)
                        }
                        customVibeInput = ""
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoralPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_custom_vibe")
            )

            Button(
                onClick = {
                    if (customVibeInput.isNotBlank()) {
                        val formatted = customVibeInput.trim()
                        customVibesList = (customVibesList + formatted).distinct()
                        if (!selectedVibes.contains(formatted) && selectedVibes.size < maxVibes) {
                            onVibesChanged(selectedVibes + formatted)
                        }
                        customVibeInput = ""
                    }
                },
                enabled = customVibeInput.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                modifier = Modifier
                    .height(52.dp)
                    .testTag("btn_add_custom_vibe")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangoutVibesSheet(
    initialVibes: List<String>,
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentVibes by remember { mutableStateOf(initialVibes) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Hangout Vibes & Interests 🎯",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HangoutVibesSelector(
                selectedVibes = currentVibes,
                onVibesChanged = { currentVibes = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onSave(currentVibes)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_vibes_sheet"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
            ) {
                Text(
                    text = "Save ${currentVibes.size} Hangout Vibes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SharedVibeChips(
    profileVibes: List<String> = emptyList(),
    userVibes: List<String> = emptyList(),
    vibes: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    val targetVibes = if (profileVibes.isNotEmpty()) profileVibes else vibes
    if (targetVibes.isEmpty()) return

    val sharedVibes = targetVibes.filter { pv ->
        userVibes.any { uv ->
            uv.equals(pv, ignoreCase = true) ||
            pv.contains(uv.filter { it.isLetter() }, ignoreCase = true) ||
            uv.contains(pv.filter { it.isLetter() }, ignoreCase = true)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (sharedVibes.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = AmberAccent.copy(alpha = 0.16f),
                border = BorderStroke(1.dp, AmberAccent.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🎯", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Shared Vibe: ${sharedVibes.joinToString(", ")}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                }
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            targetVibes.forEach { vibe ->
                val isShared = sharedVibes.contains(vibe)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isShared) CoralPrimary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(
                        1.dp,
                        if (isShared) CoralPrimary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isShared) {
                            Text(text = "✨", fontSize = 10.sp)
                        }
                        Text(
                            text = vibe,
                            fontSize = 12.sp,
                            fontWeight = if (isShared) FontWeight.Bold else FontWeight.Medium,
                            color = if (isShared) CoralPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
