package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IntentMode
import com.example.data.model.UserProfileSettings
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.VioletSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHangoutSheet(
    onDismiss: () -> Unit,
    onCreate: (title: String, description: String, location: String, mode: IntentMode, timeLabel: String, tags: List<String>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Botanical Tea House (300m away)") }
    var mode by remember { mutableStateOf(IntentMode.FRIENDSHIP) }
    var timeLabel by remember { mutableStateOf("In 30 minutes") }
    var selectedTags by remember { mutableStateOf(listOf("Coffee", "Casual")) }

    val availableTags = listOf("Coffee", "Bouldering", "Board Games", "Walk", "Food", "Sunset", "Music", "Study")

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
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Broadcast 2km Vibe Signal 📡",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Let people within your 2km radius know you're down to hang out right now or later today!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Intent Mode
            Text("Vibe Category", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = mode == IntentMode.FRIENDSHIP,
                    onClick = { mode = IntentMode.FRIENDSHIP },
                    label = { Text("🤝 Friendship / Activity") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanTertiary,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = mode == IntentMode.DATING,
                    onClick = { mode = IntentMode.DATING },
                    label = { Text("❤️ Dating / Drink") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CoralPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Activity Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Activity Title (e.g. Grabbing iced matcha & walk)") },
                placeholder = { Text("What are you planning to do?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_hangout_title"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoralPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Details / Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Short Description") },
                placeholder = { Text("Add any specifics, meeting spot details...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .testTag("input_hangout_desc"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoralPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Local Spot
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Nearby 2km Meeting Spot") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Timeframe
            OutlinedTextField(
                value = timeLabel,
                onValueChange = { timeLabel = it },
                label = { Text("When? (e.g. In 20m, 6:00 PM)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tags
            Text("Tags", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                availableTags.take(4).forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                        },
                        label = { Text(tag, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, description, location, mode, timeLabel, selectedTags)
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_publish_hangout"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == IntentMode.DATING) CoralPrimary else CyanTertiary
                )
            ) {
                Icon(imageVector = Icons.Default.NearMe, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Broadcast Signal to 2km Radius", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(
    currentSettings: UserProfileSettings?,
    onDismiss: () -> Unit,
    onSave: (UserProfileSettings) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val current = currentSettings ?: UserProfileSettings()
    var name by remember { mutableStateOf(current.name) }
    var age by remember { mutableStateOf(current.age.toString()) }
    var gender by remember { mutableStateOf(current.gender) }
    var bio by remember { mutableStateOf(current.bio) }
    var vibe by remember { mutableStateOf(current.currentVibe) }
    var lookingFor by remember { mutableStateOf(current.lookingForMode) }
    var stealthMode by remember { mutableStateOf(current.isStealthMode) }
    var showExactMeters by remember { mutableStateOf(current.showExactMeters) }
    var hangoutVibes by remember { mutableStateOf(current.hangoutVibes) }

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
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit My 2km Profile ✏️",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Name & Age
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    modifier = Modifier
                        .weight(2f)
                        .testTag("input_edit_name"),
                    shape = RoundedCornerShape(14.dp)
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it.filter { ch -> ch.isDigit() }.take(2) },
                    label = { Text("Age") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_edit_age"),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gender / Pronouns
            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender / Pronouns") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bio
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio / About Me") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .testTag("input_edit_bio"),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Current 2km Vibe Broadcast
            OutlinedTextField(
                value = vibe,
                onValueChange = { vibe = it },
                label = { Text("Current 2km Vibe Status") },
                placeholder = { Text("e.g. Free for coffee at 4 PM ☕") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hangout Vibes & Interests Tag Selection
            HangoutVibesSelector(
                selectedVibes = hangoutVibes,
                onVibesChanged = { hangoutVibes = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Intent Mode
            Text("I'm looking for:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    IntentMode.BOTH to "All (Dating & Friends)",
                    IntentMode.DATING to "❤️ Dating",
                    IntentMode.FRIENDSHIP to "🤝 Friends"
                ).forEach { (m, label) ->
                    FilterChip(
                        selected = lookingFor == m,
                        onClick = { lookingFor = m },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Privacy Switches
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Stealth Mode (Ghost on Radar)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Browse others without appearing on their 2km scanner", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = stealthMode,
                            onCheckedChange = { stealthMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = CoralPrimary, checkedTrackColor = CoralPrimary.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Show Exact Distance in Meters", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text("Shows '320m' vs general 'Nearby (<1km)'", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = showExactMeters,
                            onCheckedChange = { showExactMeters = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = CyanTertiary, checkedTrackColor = CyanTertiary.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onSave(
                        current.copy(
                            name = name.ifBlank { current.name },
                            age = age.toIntOrNull() ?: current.age,
                            gender = gender,
                            bio = bio,
                            currentVibe = vibe,
                            hangoutVibes = hangoutVibes,
                            lookingForMode = lookingFor,
                            isStealthMode = stealthMode,
                            showExactMeters = showExactMeters
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_profile"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Profile Changes", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerSheet(
    currentLocation: String,
    onDismiss: () -> Unit,
    onSelectLocation: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val locations = listOf(
        "Downtown Arts District",
        "University Campus Town",
        "Riverfront Tech Hub",
        "Historic Old Town & Cafes",
        "Waterfront Marina & Promenade",
        "Uptown Design Quarter"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Change 2km Radar Location 📍",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Simulate scanning in a different neighborhood to discover local singles and friends within that area's 2km zone.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            locations.forEach { loc ->
                val isSelected = loc.equals(currentLocation, ignoreCase = true)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) VioletSecondary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, VioletSecondary) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectLocation(loc) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null,
                            tint = if (isSelected) VioletSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = loc,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "2.0 km scanning radius active",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isSelected) VioletSecondary else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}
