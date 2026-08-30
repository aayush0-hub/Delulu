package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UserProfileSettings
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.CyanTertiary
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.VioletSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class VerificationScreen {
    HUB,
    SELFIE_POSE,
    SELFIE_CAMERA,
    SELFIE_ANALYZING,
    SELFIE_SUCCESS,
    PHONE_INPUT,
    PHONE_OTP,
    PHONE_SUCCESS
}

data class PoseChallenge(
    val emoji: String,
    val title: String,
    val instruction: String,
    val iconDescription: String
)

private val POSE_CHALLENGES = listOf(
    PoseChallenge("✌️", "Peace Sign Pose", "Hold two fingers up beside your cheek with a gentle smile", "Two fingers peace gesture"),
    PoseChallenge("👉", "Index Finger Pose", "Point your index finger upward beside your temple", "Point gesture"),
    PoseChallenge("👋", "Friendly Wave", "Raise an open hand beside your chin as if waving", "Wave gesture"),
    PoseChallenge("👍", "Thumbs Up Pose", "Give a thumbs up near your collarbone", "Thumbs up gesture")
)

@Composable
fun ProfileVerificationDialog(
    userSettings: UserProfileSettings,
    onVerifyPhone: (String) -> Unit,
    onVerifySelfie: (Int) -> Unit,
    onResetVerification: () -> Unit,
    onDismiss: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(VerificationScreen.HUB) }
    var selectedPoseIndex by remember { mutableIntStateOf(0) }
    var enteredPhone by remember { mutableStateOf(userSettings.verifiedPhoneNumber ?: "555-389-9402") }
    var selectedCountryCode by remember { mutableStateOf("+1 (US)") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpInvalid by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .height(680.dp)
                .testTag("dialog_profile_verification")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header with navigation or close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentScreen != VerificationScreen.HUB) {
                        IconButton(
                            onClick = { currentScreen = VerificationScreen.HUB },
                            modifier = Modifier.testTag("btn_verification_back")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to verification hub",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(CyanTertiary, VioletSecondary)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "Profile Verification",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_verification")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Content View Switcher
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
                    label = "verification_transition",
                    modifier = Modifier.weight(1f)
                ) { screen ->
                    when (screen) {
                        VerificationScreen.HUB -> VerificationHubView(
                            userSettings = userSettings,
                            onStartSelfie = {
                                selectedPoseIndex = (0 until POSE_CHALLENGES.size).random()
                                currentScreen = VerificationScreen.SELFIE_POSE
                            },
                            onStartPhone = {
                                currentScreen = VerificationScreen.PHONE_INPUT
                            },
                            onReset = onResetVerification
                        )

                        VerificationScreen.SELFIE_POSE -> SelfiePoseInstructionView(
                            challenge = POSE_CHALLENGES[selectedPoseIndex],
                            onStartCamera = {
                                currentScreen = VerificationScreen.SELFIE_CAMERA
                            },
                            onChangePose = {
                                selectedPoseIndex = (selectedPoseIndex + 1) % POSE_CHALLENGES.size
                            }
                        )

                        VerificationScreen.SELFIE_CAMERA -> SelfieCameraScannerView(
                            challenge = POSE_CHALLENGES[selectedPoseIndex],
                            avatarEmoji = userSettings.avatarEmoji,
                            avatarColor = userSettings.avatarColor,
                            onCapture = {
                                currentScreen = VerificationScreen.SELFIE_ANALYZING
                            }
                        )

                        VerificationScreen.SELFIE_ANALYZING -> SelfieAnalyzingView(
                            userName = userSettings.name,
                            avatarEmoji = userSettings.avatarEmoji,
                            onAnalysisComplete = { score ->
                                onVerifySelfie(score)
                                currentScreen = VerificationScreen.SELFIE_SUCCESS
                            }
                        )

                        VerificationScreen.SELFIE_SUCCESS -> VerificationSuccessView(
                            title = "Selfie Match Verified! ✨",
                            description = "Your facial biometric scan matched your profile avatar with 98.6% confidence. The Blue Verification Badge is now active on your radar blip!",
                            badgeLabel = "✓ Selfie Verified (98% Match)",
                            onDone = { currentScreen = VerificationScreen.HUB }
                        )

                        VerificationScreen.PHONE_INPUT -> PhoneInputView(
                            phoneNumber = enteredPhone,
                            onPhoneChange = { enteredPhone = it },
                            countryCode = selectedCountryCode,
                            onCountryCodeChange = { selectedCountryCode = it },
                            onSendCode = {
                                otpCode = ""
                                isOtpInvalid = false
                                currentScreen = VerificationScreen.PHONE_OTP
                            }
                        )

                        VerificationScreen.PHONE_OTP -> PhoneOtpView(
                            phoneNumber = "$selectedCountryCode $enteredPhone",
                            otpCode = otpCode,
                            onOtpChange = {
                                otpCode = it
                                isOtpInvalid = false
                            },
                            isInvalid = isOtpInvalid,
                            onVerifyCode = { code ->
                                if (code.trim() == "749201" || code.trim().length >= 4) {
                                    onVerifyPhone("$selectedCountryCode $enteredPhone")
                                    currentScreen = VerificationScreen.PHONE_SUCCESS
                                } else {
                                    isOtpInvalid = true
                                }
                            },
                            onResendCode = {
                                otpCode = "749201"
                            }
                        )

                        VerificationScreen.PHONE_SUCCESS -> VerificationSuccessView(
                            title = "Phone Confirmed! 📱",
                            description = "Your phone number has been authenticated. Other users will see that your profile is confirmed and backed by a real number.",
                            badgeLabel = "✓ Phone Verified ($selectedCountryCode $enteredPhone)",
                            onDone = { currentScreen = VerificationScreen.HUB }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerificationHubView(
    userSettings: UserProfileSettings,
    onStartSelfie: () -> Unit,
    onStartPhone: () -> Unit,
    onReset: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isFullyVerified = userSettings.isPhoneVerified && userSettings.isPhotoVerified
    val verifiedCount = (if (userSettings.isPhoneVerified) 1 else 0) + (if (userSettings.isPhotoVerified) 1 else 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Trust Score Hero Banner
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isFullyVerified) CyanTertiary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isFullyVerified) "Trust Level: Maximum 🛡️" else "Verification Progress",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$verifiedCount of 2 trust badges earned",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isFullyVerified) CyanTertiary.copy(alpha = 0.2f) else CoralPrimary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (isFullyVerified) "100% Trust" else "${verifiedCount * 50}% Trust",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFullyVerified) CyanTertiary else CoralPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { verifiedCount / 2f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CyanTertiary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Verification Methods",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Track 1: Live Selfie Cross-Reference
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (userSettings.isPhotoVerified) CyanTertiary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(VioletSecondary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            tint = VioletSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Selfie Photo Cross-Reference",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (userSettings.isPhotoVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = CyanTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = if (userSettings.isPhotoVerified) "Cross-referenced with 98% match confidence" else "Take a quick live pose selfie to prove authenticity",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onStartSelfie,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userSettings.isPhotoVerified) MaterialTheme.colorScheme.surfaceVariant else VioletSecondary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_start_selfie_verification")
                ) {
                    Icon(
                        imageVector = if (userSettings.isPhotoVerified) Icons.Default.Refresh else Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = if (userSettings.isPhotoVerified) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (userSettings.isPhotoVerified) "Re-verify Selfie Photo" else "Verify with Live Selfie (Instant)",
                        color = if (userSettings.isPhotoVerified) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Track 2: Phone Confirmation
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (userSettings.isPhoneVerified) CyanTertiary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(CyanTertiary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = CyanTertiary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SMS Phone Confirmation",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (userSettings.isPhoneVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = CyanTertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = if (userSettings.isPhoneVerified) "Confirmed with ${userSettings.verifiedPhoneNumber}" else "Authenticate your profile with a quick SMS passcode",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onStartPhone,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userSettings.isPhoneVerified) MaterialTheme.colorScheme.surfaceVariant else CyanTertiary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_start_phone_verification")
                ) {
                    Icon(
                        imageVector = if (userSettings.isPhoneVerified) Icons.Default.Refresh else Icons.Default.Phone,
                        contentDescription = null,
                        tint = if (userSettings.isPhoneVerified) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (userSettings.isPhoneVerified) "Change Phone Number" else "Confirm Phone Number (SMS)",
                        color = if (userSettings.isPhoneVerified) MaterialTheme.colorScheme.onSurfaceVariant else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Why Verify Box
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AmberAccent.copy(alpha = 0.1f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = AmberAccent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Verified profiles within 2km receive 3x more connection requests and appear prominently on the live radar.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            }
        }

        if (userSettings.isPartiallyVerified) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_reset_verification")
            ) {
                Text("Reset All Verification Status (Testing Demo)", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SelfiePoseInstructionView(
    challenge: PoseChallenge,
    onStartCamera: () -> Unit,
    onChangePose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = VioletSecondary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "STEP 1 OF 2: POSE CHALLENGE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Copy the Anti-Spoof Pose",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "To verify you are real, mimic the required pose in front of the camera.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Pose Challenge Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, VioletSecondary.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = challenge.emoji,
                        fontSize = 72.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = challenge.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = challenge.instruction,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onChangePose,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_change_pose")
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Try Different Pose", fontSize = 12.sp)
            }
        }

        Button(
            onClick = onStartCamera,
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_open_camera")
        ) {
            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open Selfie Scanner", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SelfieCameraScannerView(
    challenge: PoseChallenge,
    avatarEmoji: String,
    avatarColor: Long,
    onCapture: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Target pose banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = challenge.emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Match Pose: ${challenge.title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Align face in the oval frame below",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Live Simulated Viewfinder Frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF141414))
                .border(2.dp, CyanTertiary.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Live scanning animation canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw face oval alignment guide
                val ovalCenter = Offset(w / 2f, h / 2f)
                val ovalWidth = w * 0.58f
                val ovalHeight = h * 0.68f

                drawOval(
                    color = Color.Cyan.copy(alpha = 0.6f),
                    topLeft = Offset(ovalCenter.x - ovalWidth / 2f, ovalCenter.y - ovalHeight / 2f),
                    size = androidx.compose.ui.geometry.Size(ovalWidth, ovalHeight),
                    style = Stroke(
                        width = 3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
                    )
                )

                // Laser scan line
                val scanY = h * laserY
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, CyanTertiary, CoralPrimary, CyanTertiary, Color.Transparent)
                    ),
                    start = Offset(w * 0.15f, scanY),
                    end = Offset(w * 0.85f, scanY),
                    strokeWidth = 3.dp.toPx()
                )

                // Corner crosshairs
                val pad = 24.dp.toPx()
                val len = 20.dp.toPx()
                val strokeW = 3.dp.toPx()
                // Top-left
                drawLine(CyanTertiary, Offset(pad, pad), Offset(pad + len, pad), strokeW)
                drawLine(CyanTertiary, Offset(pad, pad), Offset(pad, pad + len), strokeW)
                // Top-right
                drawLine(CyanTertiary, Offset(w - pad, pad), Offset(w - pad - len, pad), strokeW)
                drawLine(CyanTertiary, Offset(w - pad, pad), Offset(w - pad, pad + len), strokeW)
                // Bottom-left
                drawLine(CyanTertiary, Offset(pad, h - pad), Offset(pad + len, h - pad), strokeW)
                drawLine(CyanTertiary, Offset(pad, h - pad), Offset(pad, h - pad - len), strokeW)
                // Bottom-right
                drawLine(CyanTertiary, Offset(w - pad, h - pad), Offset(w - pad - len, h - pad), strokeW)
                drawLine(CyanTertiary, Offset(w - pad, h - pad), Offset(w - pad, h - pad - len), strokeW)
            }

            // User face representation with pose emoji overlay
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(avatarColor).copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = avatarEmoji, fontSize = 54.sp)
                    }

                    // Pose overlay
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = challenge.emoji,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldGreen.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lighting: Optimal ☀️ • Face Detected", fontSize = 11.sp, color = EmeraldGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Capture Shutter Button
        Button(
            onClick = onCapture,
            colors = ButtonDefaults.buttonColors(containerColor = CyanTertiary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_capture_selfie")
        ) {
            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Capture & Cross-Reference", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SelfieAnalyzingView(
    userName: String,
    avatarEmoji: String,
    onAnalysisComplete: (Int) -> Unit
) {
    var progress by remember { mutableStateOf(0.1f) }
    var currentStatusText by remember { mutableStateOf("Detecting 68 3D biometric facial landmarks...") }
    var matchScore by remember { mutableIntStateOf(98) }

    LaunchedEffect(Unit) {
        delay(600)
        progress = 0.35f
        currentStatusText = "Checking anti-spoofing depth & pose challenge..."
        delay(700)
        progress = 0.70f
        currentStatusText = "Cross-referencing face structure with $userName's avatar..."
        delay(800)
        progress = 1.0f
        currentStatusText = "Biometric Match Confirmed (98.6% confidence)!"
        delay(600)
        onAnalysisComplete(matchScore)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(CyanTertiary.copy(alpha = 0.3f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(90.dp),
                color = CyanTertiary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 6.dp
            )
            Text(text = avatarEmoji, fontSize = 36.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "AI Biometric Verification",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentStatusText,
            fontSize = 13.sp,
            color = CyanTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                BiometricCheckItem(label = "Pose Challenge Check", isDone = progress >= 0.35f)
                Spacer(modifier = Modifier.height(6.dp))
                BiometricCheckItem(label = "Facial Geometry & Depth", isDone = progress >= 0.70f)
                Spacer(modifier = Modifier.height(6.dp))
                BiometricCheckItem(label = "Profile Picture Cross-Match", isDone = progress >= 1.0f)
            }
        }
    }
}

@Composable
private fun BiometricCheckItem(label: String, isDone: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (isDone) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("PASSED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
            }
        } else {
            Text("SCANNING...", fontSize = 11.sp, color = AmberAccent, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PhoneInputView(
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    countryCode: String,
    onCountryCodeChange: (String) -> Unit,
    onSendCode: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val countries = listOf("+1 (US/CA)", "+44 (UK)", "+49 (DE)", "+33 (FR)", "+81 (JP)", "+61 (AU)", "+91 (IN)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CyanTertiary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "STEP 1 OF 2: PHONE CONFIRMATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanTertiary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "What is your phone number?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "We'll send a 6-digit SMS verification code. Your number will never be publicly displayed.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { dropdownExpanded = true }
                    ) {
                        Text(
                            text = countryCode,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text(country) },
                                onClick = {
                                    onCountryCodeChange(country)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = onPhoneChange,
                    placeholder = { Text("555-0192") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanTertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_phone_number")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = CyanTertiary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Zero-Knowledge Encryption: Phone is strictly used for one-time identity verification.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Button(
            onClick = onSendCode,
            colors = ButtonDefaults.buttonColors(containerColor = CyanTertiary),
            shape = RoundedCornerShape(14.dp),
            enabled = phoneNumber.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_send_sms_code")
        ) {
            Text("Send 6-Digit SMS Code", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PhoneOtpView(
    phoneNumber: String,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    isInvalid: Boolean,
    onVerifyCode: (String) -> Unit,
    onResendCode: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(45) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CyanTertiary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "STEP 2 OF 2: ENTER OTP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanTertiary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter 6-Digit Code",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sent to $phoneNumber",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = {
                    if (it.length <= 6) {
                        onOtpChange(it)
                    }
                },
                placeholder = { Text("749201", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                isError = isInvalid,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanTertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_otp_code")
            )

            if (isInvalid) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Invalid code. Try using demo code: 749201",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Demo Quick Fill helper
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = VioletSecondary.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, VioletSecondary.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        onOtpChange("749201")
                    }
                    .testTag("btn_quick_fill_demo_otp")
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Demo Code is 749201 (Tap to fill automatically)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VioletSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (secondsLeft > 0) {
                    Text(
                        text = "Resend SMS in ${secondsLeft}s",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Resend Code",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanTertiary,
                        modifier = Modifier
                            .clickable {
                                secondsLeft = 45
                                onResendCode()
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        Button(
            onClick = { onVerifyCode(otpCode) },
            colors = ButtonDefaults.buttonColors(containerColor = CyanTertiary),
            shape = RoundedCornerShape(14.dp),
            enabled = otpCode.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_confirm_otp")
        ) {
            Text("Confirm & Verify Phone", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun VerificationSuccessView(
    title: String,
    description: String,
    badgeLabel: String,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(CyanTertiary, VioletSecondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CyanTertiary.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanTertiary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = CyanTertiary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = badgeLabel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanTertiary
                    )
                }
            }
        }

        Button(
            onClick = onDone,
            colors = ButtonDefaults.buttonColors(containerColor = CyanTertiary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_verification_done")
        ) {
            Text("Done", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
