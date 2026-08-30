package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.IntentMode
import com.example.data.model.Match
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.VioletSecondary

@Composable
fun MatchCelebrationDialog(
    match: Match?,
    userAvatarEmoji: String = "✨",
    userAvatarColor: Long = 0xFFFF4081,
    onOpenChat: (Match) -> Unit,
    onDismiss: () -> Unit
) {
    if (match == null) return

    Dialog(onDismissRequest = onDismiss) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + scaleIn(initialScale = 0.85f)
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .testTag("dialog_match_celebration")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header tag
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = (if (match.mode == IntentMode.DATING) CoralPrimary else CyanTertiary).copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (match.mode == IntentMode.DATING) Icons.Default.Favorite else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (match.mode == IntentMode.DATING) CoralPrimary else CyanTertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (match.mode == IntentMode.DATING) "2KM DATING MATCH!" else "2KM FRIEND CONNECTED!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (match.mode == IntentMode.DATING) CoralPrimary else CyanTertiary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Connected Avatars Overlapping
                    Box(
                        modifier = Modifier.height(88.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // User Avatar
                            AvatarBadge(
                                avatarEmoji = userAvatarEmoji,
                                avatarColor = userAvatarColor,
                                size = 72.dp,
                                showOnlineDot = false
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Heart / Handshake Badge in between
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(CoralPrimary, VioletSecondary)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (match.mode == IntentMode.DATING) "❤️" else "🤝",
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Matched Person Avatar
                            AvatarBadge(
                                avatarEmoji = match.avatarEmoji,
                                avatarColor = match.avatarColor,
                                size = 72.dp,
                                showOnlineDot = false
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "You & ${match.name}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            tint = CyanTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Only ${match.distanceMeters}m apart right now!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyanTertiary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (match.mode == IntentMode.DATING)
                            "You both showed interest in each other's 2km radar profile. Time to break the ice!"
                        else
                            "You both matched nearby for friendship & activities. Say hi and grab coffee or climb!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Button(
                        onClick = {
                            onDismiss()
                            onOpenChat(match)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_match_chat"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoralPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Chat",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send a Message", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_match_keep_exploring"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Keep Exploring 2km Radar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
