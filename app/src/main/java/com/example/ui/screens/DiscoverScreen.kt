package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IntentMode
import com.example.data.model.Profile
import com.example.ui.UiState
import com.example.ui.components.AvatarBadge
import com.example.ui.components.DistanceBadge
import com.example.ui.components.ModeFilterBar
import com.example.ui.components.ModeTag
import com.example.ui.components.SharedVibeChips
import com.example.ui.components.TagChips
import com.example.ui.components.VerifiedBadge
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.VioletSecondary

@Composable
fun DiscoverScreen(
    uiState: UiState,
    onSelectMode: (IntentMode?) -> Unit,
    onToggleVerifiedOnly: () -> Unit,
    onSelectProfile: (Profile?) -> Unit,
    onLikeProfile: (Profile) -> Unit,
    onPassProfile: (String) -> Unit,
    onResetProfiles: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isGridView by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 72.dp)
    ) {
        // Mode filter + Grid/Card view toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ModeFilterBar(
                    selectedMode = uiState.selectedModeFilter,
                    onSelectMode = onSelectMode,
                    verifiedOnly = uiState.verifiedOnlyFilter,
                    onToggleVerifiedOnly = onToggleVerifiedOnly
                )
            }
            IconButton(
                onClick = { isGridView = !isGridView },
                modifier = Modifier.testTag("toggle_view_mode")
            ) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.ViewCarousel else Icons.Default.GridView,
                    contentDescription = "Toggle Grid/Deck View",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (uiState.filteredProfiles.isEmpty()) {
            // Empty state when all profiles swiped or filtered out
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🛰️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No More Profiles in Range",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "You've reviewed all local profiles within your 2.0 km radius. Check back later or reset to browse again.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onResetProfiles,
                            colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("btn_reset_deck")
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset & Rescan 2km Deck")
                        }
                    }
                }
            }
        } else if (isGridView) {
            // Grid view of profiles
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.filteredProfiles, key = { it.id }) { profile ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onSelectProfile(profile) }
                            .testTag("card_grid_profile_${profile.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AvatarBadge(
                                avatarEmoji = profile.avatarEmoji,
                                avatarColor = profile.avatarColor,
                                size = 64.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${profile.name}, ${profile.age}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (profile.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    VerifiedBadge(
                                        isVerified = true,
                                        verificationMethod = profile.verificationMethod,
                                        photoMatchScore = profile.photoMatchScore,
                                        compact = true
                                    )
                                }
                            }
                            Text(
                                text = profile.occupation,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            DistanceBadge(distanceMeters = profile.distanceMeters)
                            Spacer(modifier = Modifier.height(6.dp))
                            ModeTag(mode = profile.mode)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(
                                    onClick = { onPassProfile(profile.id) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Pass",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onLikeProfile(profile) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            if (profile.mode == IntentMode.DATING) CoralPrimary else CyanTertiary,
                                            CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = if (profile.mode == IntentMode.DATING) Icons.Default.Favorite else Icons.Default.WavingHand,
                                        contentDescription = "Connect",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Deck View: Show the top profile in full card format
            val topProfile = uiState.filteredProfiles.first()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp)
                        .testTag("card_discover_top")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Hero Header with Avatar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(topProfile.avatarColor),
                                            Color(topProfile.avatarColor).copy(alpha = 0.5f),
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = topProfile.avatarEmoji,
                                    fontSize = 72.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.Black.copy(alpha = 0.35f)
                                ) {
                                    Text(
                                        text = topProfile.lastActive,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Column(modifier = Modifier.padding(18.dp)) {
                            // Name & Distance & Verification Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${topProfile.name}, ${topProfile.age}",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (topProfile.isVerified) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            VerifiedBadge(
                                                isVerified = true,
                                                verificationMethod = topProfile.verificationMethod,
                                                photoMatchScore = topProfile.photoMatchScore,
                                                compact = false
                                            )
                                        }
                                    }
                                    Text(
                                        text = topProfile.occupation,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                DistanceBadge(distanceMeters = topProfile.distanceMeters)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ModeTag(mode = topProfile.mode)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Text(
                                        text = topProfile.relationshipGoal,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 2km Vibe quote
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = CoralPrimary.copy(alpha = 0.1f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "✨", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = topProfile.vibeStatus,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Bio
                            Text(
                                text = topProfile.bio,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Hangout Vibes
                            if (topProfile.hangoutVibes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = "🎯", fontSize = 13.sp)
                                    Text(
                                        text = "Hangout Vibes",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                SharedVibeChips(vibes = topProfile.hangoutVibes)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Interests
                            TagChips(tags = topProfile.interests)

                            Spacer(modifier = Modifier.height(14.dp))

                            // Icebreaker answer card
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = VioletSecondary.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = topProfile.icebreakerPrompt,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VioletSecondary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "\"${topProfile.icebreakerAnswer}\"",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Favorite local spot
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = CyanTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Favorite Spot: ${topProfile.favoriteSpotWithin2km}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CyanTertiary
                                )
                            }
                        }
                    }
                }

                // Floating Action Bar (Pass, More Info, Like)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pass Button
                    FloatingActionButton(
                        onClick = { onPassProfile(topProfile.id) },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color(0xFFEF4444),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(54.dp)
                            .testTag("btn_deck_pass")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Pass", modifier = Modifier.size(24.dp))
                    }

                    // More Details Button
                    FloatingActionButton(
                        onClick = { onSelectProfile(topProfile) },
                        containerColor = VioletSecondary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(46.dp)
                            .testTag("btn_deck_details")
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Full Details", modifier = Modifier.size(20.dp))
                    }

                    // Like / Connect Button
                    FloatingActionButton(
                        onClick = { onLikeProfile(topProfile) },
                        containerColor = if (topProfile.mode == IntentMode.DATING) CoralPrimary else CyanTertiary,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(58.dp)
                            .testTag("btn_deck_like")
                    ) {
                        Icon(
                            imageVector = if (topProfile.mode == IntentMode.DATING) Icons.Default.Favorite else Icons.Default.WavingHand,
                            contentDescription = "Like / Connect",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
