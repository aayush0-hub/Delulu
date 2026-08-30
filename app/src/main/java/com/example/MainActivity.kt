package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.components.CreateHangoutSheet
import com.example.ui.components.DeluluGuideSheet
import com.example.ui.components.EditProfileSheet
import com.example.ui.components.LocationPickerSheet
import com.example.ui.components.MatchCelebrationDialog
import com.example.ui.components.ProfileDetailSheet
import com.example.ui.components.ProfileVerificationDialog
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.HangoutsScreen
import com.example.ui.screens.MatchesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RadarScreen
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VioletSecondary

enum class AppTab(val title: String, val icon: ImageVector, val tag: String) {
    RADAR("Radar", Icons.Default.Radar, "tab_radar"),
    DISCOVER("Discover", Icons.Default.Explore, "tab_discover"),
    HANGOUTS("Hangouts", Icons.Default.Sensors, "tab_hangouts"),
    CHATS("Chats", Icons.Default.Chat, "tab_chats"),
    PROFILE("Profile", Icons.Default.Person, "tab_profile")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                TwoKmApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoKmApp(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(AppTab.RADAR) }
    var showGuideSheet by remember { mutableStateOf(false) }

    // If an active chat conversation is open, render ChatScreen
    val activeChat = uiState.activeMatchChat
    if (activeChat != null) {
        ChatScreen(
            match = activeChat,
            viewModel = viewModel,
            onBack = { viewModel.openChat(null) }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(CoralPrimary, VioletSecondary)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✨",
                                    fontSize = 17.sp
                                )
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Delulu",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = CoralPrimary.copy(alpha = 0.18f)
                                    ) {
                                        Text(
                                            text = "2.0 km",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CoralPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = when (selectedTab) {
                                        AppTab.RADAR -> "Live Radar • Tap blips"
                                        AppTab.DISCOVER -> "Discover • Nearby Cards"
                                        AppTab.HANGOUTS -> "Hangout Beacons • Join"
                                        AppTab.CHATS -> "Connections • Instant Chat"
                                        AppTab.PROFILE -> "My Vibe & 2km Settings"
                                    },
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    actions = {
                        // Quick location picker pill
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { viewModel.setShowLocationPicker(true) }
                                .testTag("btn_top_bar_location")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditLocation,
                                    contentDescription = "Change Location",
                                    tint = CoralPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = (uiState.userSettings?.currentLocationName ?: "Downtown").take(12),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Guide & Tips Button
                        IconButton(
                            onClick = { showGuideSheet = true },
                            modifier = Modifier.testTag("btn_open_delulu_guide")
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Delulu Guide & Tips",
                                tint = CyanTertiary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar")
                ) {
                    AppTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        val totalUnread = uiState.matches.sumOf { it.unreadCount }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            icon = {
                                if (tab == AppTab.CHATS && totalUnread > 0) {
                                    BadgedBox(badge = {
                                        Badge(containerColor = CoralPrimary) {
                                            Text("$totalUnread", fontSize = 10.sp, color = Color.White)
                                        }
                                    }) {
                                        Icon(imageVector = tab.icon, contentDescription = tab.title)
                                    }
                                } else {
                                    Icon(imageVector = tab.icon, contentDescription = tab.title)
                                }
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag(tab.tag)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        AppTab.RADAR -> RadarScreen(
                            uiState = uiState,
                            onSelectMode = { viewModel.setModeFilter(it) },
                            onSelectProfile = { viewModel.selectProfile(it) },
                            onLikeProfile = { viewModel.likeProfile(it) },
                            onPassProfile = { viewModel.passProfile(it) },
                            onToggleScanning = { viewModel.toggleScanning() },
                            onChangeLocationClick = { viewModel.setShowLocationPicker(true) }
                        )

                        AppTab.DISCOVER -> DiscoverScreen(
                            uiState = uiState,
                            onSelectMode = { viewModel.setModeFilter(it) },
                            onToggleVerifiedOnly = { viewModel.toggleVerifiedOnlyFilter() },
                            onSelectProfile = { viewModel.selectProfile(it) },
                            onLikeProfile = { viewModel.likeProfile(it) },
                            onPassProfile = { viewModel.passProfile(it) },
                            onResetProfiles = { viewModel.resetAllProfiles() }
                        )

                        AppTab.HANGOUTS -> HangoutsScreen(
                            uiState = uiState,
                            onSelectMode = { viewModel.setModeFilter(it) },
                            onToggleJoin = { viewModel.toggleJoinHangout(it) },
                            onOpenCreateHangout = { viewModel.setShowCreateHangout(true) }
                        )

                        AppTab.CHATS -> MatchesScreen(
                            uiState = uiState,
                            onSelectMode = { viewModel.setModeFilter(it) },
                            onToggleVerifiedOnly = { viewModel.toggleVerifiedOnlyFilter() },
                            onOpenChat = { viewModel.openChat(it) },
                            onNavigateToRadar = { selectedTab = AppTab.RADAR }
                        )

                        AppTab.PROFILE -> ProfileScreen(
                            uiState = uiState,
                            onEditProfileClick = { viewModel.setShowEditProfile(true) },
                            onOpenVerification = { viewModel.setShowVerificationDialog(true) },
                            onChangeLocationClick = { viewModel.setShowLocationPicker(true) },
                            onRadiusChange = { viewModel.setMaxRadius(it) },
                            onResetAllData = { viewModel.resetAllProfiles() },
                            onUpdateVibes = { viewModel.updateHangoutVibes(it) }
                        )
                    }
                }
            }
        }
    }

    // Sheets & Dialogs
    val userSettings = uiState.userSettings
    if (uiState.showVerificationDialog && userSettings != null) {
        ProfileVerificationDialog(
            userSettings = userSettings,
            onDismiss = { viewModel.setShowVerificationDialog(false) },
            onVerifyPhone = { phone ->
                viewModel.verifyPhoneNumber(phone)
            },
            onVerifySelfie = { score ->
                viewModel.verifySelfiePhoto(score)
            },
            onResetVerification = {
                viewModel.resetVerification()
            }
        )
    }

    ProfileDetailSheet(
        profile = uiState.selectedProfile,
        onDismiss = { viewModel.selectProfile(null) },
        onLike = { viewModel.likeProfile(it) },
        onPass = { viewModel.passProfile(it) }
    )

    MatchCelebrationDialog(
        match = uiState.newMatchCelebration,
        userAvatarEmoji = uiState.userSettings?.avatarEmoji ?: "✨",
        userAvatarColor = uiState.userSettings?.avatarColor ?: 0xFFFF4081,
        onOpenChat = { match ->
            viewModel.dismissMatchCelebration()
            viewModel.openChat(match)
        },
        onDismiss = { viewModel.dismissMatchCelebration() }
    )

    if (uiState.showCreateHangoutDialog) {
        CreateHangoutSheet(
            onDismiss = { viewModel.setShowCreateHangout(false) },
            onCreate = { title, desc, loc, mode, time, tags ->
                viewModel.createHangoutSignal(title, desc, loc, mode, time, tags)
            }
        )
    }

    if (uiState.showEditProfileDialog) {
        EditProfileSheet(
            currentSettings = uiState.userSettings,
            onDismiss = { viewModel.setShowEditProfile(false) },
            onSave = { updated ->
                viewModel.saveProfileSettings(updated)
            }
        )
    }

    if (uiState.showLocationPicker) {
        LocationPickerSheet(
            currentLocation = uiState.userSettings?.currentLocationName ?: "Downtown Arts District",
            onDismiss = { viewModel.setShowLocationPicker(false) },
            onSelectLocation = { newLoc ->
                viewModel.relocateTo(newLoc)
            }
        )
    }

    if (showGuideSheet) {
        DeluluGuideSheet(
            onDismiss = { showGuideSheet = false },
            onOpenRadar = {
                showGuideSheet = false
                selectedTab = AppTab.RADAR
            },
            onOpenHangouts = {
                showGuideSheet = false
                selectedTab = AppTab.HANGOUTS
            }
        )
    }
}
