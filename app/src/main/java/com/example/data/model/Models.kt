package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class IntentMode {
    DATING,
    FRIENDSHIP,
    BOTH
}

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val occupation: String,
    val bio: String,
    val mode: IntentMode,
    val relationshipGoal: String, // e.g. "Looking for long term", "Coffee & walking companion"
    val interests: List<String>,
    val avatarColor: Long, // Color ARGB long for avatar generation
    val avatarEmoji: String,
    val distanceMeters: Int, // 50m to 2000m
    val angleDegrees: Float, // for radar polar coordinate positioning (0..360)
    val distanceRatio: Float, // 0.0f..1.0f (relative to 2000m)
    val lastActive: String, // e.g. "Active 3m ago"
    val vibeStatus: String, // e.g. "Craving iced matcha latte", "Free after 6 PM"
    val icebreakerPrompt: String,
    val icebreakerAnswer: String,
    val favoriteSpotWithin2km: String,
    val hangoutVibes: List<String> = emptyList(),
    val isLiked: Boolean = false,
    val isPassed: Boolean = false,
    val isMatched: Boolean = false,
    val isVerified: Boolean = false,
    val verificationMethod: String = "", // e.g. "Live Selfie Match", "Phone Confirmed", "Selfie + Phone"
    val photoMatchScore: Int = 0 // e.g. 98
)

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val profileId: String,
    val name: String,
    val age: Int,
    val avatarColor: Long,
    val avatarEmoji: String,
    val mode: IntentMode,
    val distanceMeters: Int,
    val matchedAt: Long = System.currentTimeMillis(),
    val lastMessage: String = "You connected on 2km radar!",
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isVerified: Boolean = false,
    val verificationMethod: String = "",
    val photoMatchScore: Int = 0
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchId: String,
    val senderIsUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isIcebreaker: Boolean = false,
    val suggestedLocation: String? = null
)

@Entity(tableName = "hangout_signals")
data class HangoutSignal(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val creatorName: String,
    val creatorAvatarEmoji: String,
    val creatorColor: Long,
    val mode: IntentMode,
    val title: String,
    val description: String,
    val locationName: String,
    val distanceMeters: Int,
    val timeLabel: String, // e.g. "Happening in 25m", "Starts at 7:00 PM"
    val tags: List<String>,
    val attendeesCount: Int = 1,
    val isJoined: Boolean = false
)

@Entity(tableName = "user_settings")
data class UserProfileSettings(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex",
    val age: Int = 24,
    val gender: String = "Non-binary",
    val bio: String = "Graphic designer who loves specialty coffee, indie pop & bouldering. Exploring 2km radius!",
    val lookingForMode: IntentMode = IntentMode.BOTH,
    val radarRadiusMeters: Int = 2000,
    val currentVibe: String = "Exploring nearby cafes ☕",
    val isStealthMode: Boolean = false,
    val showExactMeters: Boolean = true,
    val currentLocationName: String = "Downtown Arts District",
    val avatarEmoji: String = "✨",
    val avatarColor: Long = 0xFFFF4081,
    val hangoutVibes: List<String> = listOf("☕ Coffee", "🌿 Walk in the park", "🎲 Game night"),
    val isPhoneVerified: Boolean = false,
    val verifiedPhoneNumber: String? = null,
    val isPhotoVerified: Boolean = false,
    val photoMatchScore: Int = 0,
    val verificationDate: String? = null
) {
    val isFullyVerified: Boolean
        get() = isPhoneVerified && isPhotoVerified

    val isPartiallyVerified: Boolean
        get() = isPhoneVerified || isPhotoVerified

    val verificationSummary: String
        get() = when {
            isFullyVerified -> "Fully Verified (Selfie + Phone)"
            isPhotoVerified -> "Photo Verified (Selfie Match $photoMatchScore%)"
            isPhoneVerified -> "Phone Verified ($verifiedPhoneNumber)"
            else -> "Not Verified"
        }
}
