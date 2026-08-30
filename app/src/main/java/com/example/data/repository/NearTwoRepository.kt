package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SeedData
import com.example.data.model.ChatMessage
import com.example.data.model.HangoutSignal
import com.example.data.model.IntentMode
import com.example.data.model.Match
import com.example.data.model.Profile
import com.example.data.model.UserProfileSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class NearTwoRepository(private val database: AppDatabase) {

    private val profileDao = database.profileDao()
    private val matchDao = database.matchDao()
    private val chatMessageDao = database.chatMessageDao()
    private val hangoutSignalDao = database.hangoutSignalDao()
    private val userProfileDao = database.userProfileDao()

    val allProfiles: Flow<List<Profile>> = profileDao.getAllActiveProfiles()
    val allMatches: Flow<List<Match>> = matchDao.getAllMatches()
    val allHangouts: Flow<List<HangoutSignal>> = hangoutSignalDao.getAllHangouts()
    val userSettings: Flow<UserProfileSettings?> = userProfileDao.getUserSettings()

    suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        if (profileDao.countProfiles() == 0) {
            profileDao.insertProfiles(SeedData.getInitialProfiles())
        }
        if (hangoutSignalDao.countHangouts() == 0) {
            hangoutSignalDao.insertHangouts(SeedData.getInitialHangouts())
        }
        if (userProfileDao.getUserSettingsDirect() == null) {
            userProfileDao.saveUserSettings(SeedData.getDefaultUser())
        }
        val currentMatches = matchDao.getAllMatches().firstOrNull()
        if (currentMatches.isNullOrEmpty()) {
            SeedData.getInitialMatches().forEach { matchDao.insertMatch(it) }
            SeedData.getInitialMessages().forEach { chatMessageDao.insertMessage(it) }
        }
    }

    fun getMessagesForMatch(matchId: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesForMatch(matchId)
    }

    suspend fun likeProfile(profile: Profile): Match? = withContext(Dispatchers.IO) {
        val willMatch = Random.nextFloat() > 0.15f // 85% match rate in demo
        profileDao.likeProfile(profile.id, willMatch)

        if (willMatch) {
            val match = Match(
                id = "match_${profile.id}",
                profileId = profile.id,
                name = profile.name,
                age = profile.age,
                avatarColor = profile.avatarColor,
                avatarEmoji = profile.avatarEmoji,
                mode = profile.mode,
                distanceMeters = profile.distanceMeters,
                matchedAt = System.currentTimeMillis(),
                lastMessage = "You both connected on the 2km radar! Say hi!",
                lastMessageTimestamp = System.currentTimeMillis(),
                unreadCount = 1,
                isVerified = profile.isVerified,
                verificationMethod = profile.verificationMethod,
                photoMatchScore = profile.photoMatchScore
            )
            matchDao.insertMatch(match)

            // Seed initial greeting message
            val firstMsg = if (profile.mode == IntentMode.DATING) {
                "Hey! Noticed we're only ${profile.distanceMeters}m away from each other 😊 Loved your profile!"
            } else {
                "Hey! Cool to find someone nearby interested in ${profile.interests.firstOrNull() ?: "hanging out"}! 🙌"
            }
            chatMessageDao.insertMessage(
                ChatMessage(
                    matchId = match.id,
                    senderIsUser = false,
                    text = firstMsg,
                    timestamp = System.currentTimeMillis()
                )
            )
            return@withContext match
        }
        null
    }

    suspend fun passProfile(profileId: String) = withContext(Dispatchers.IO) {
        profileDao.passProfile(profileId)
    }

    suspend fun resetAllSwipes() = withContext(Dispatchers.IO) {
        profileDao.resetProfiles()
    }

    suspend fun markMatchAsRead(matchId: String) = withContext(Dispatchers.IO) {
        matchDao.clearUnread(matchId)
    }

    suspend fun sendMessage(
        matchId: String,
        text: String,
        suggestedLocation: String? = null
    ) = withContext(Dispatchers.IO) {
        val message = ChatMessage(
            matchId = matchId,
            senderIsUser = true,
            text = text,
            timestamp = System.currentTimeMillis(),
            isIcebreaker = false,
            suggestedLocation = suggestedLocation
        )
        chatMessageDao.insertMessage(message)
        matchDao.updateLastMessage(matchId, text, System.currentTimeMillis())

        // Trigger auto-reply simulation
        CoroutineScope(Dispatchers.IO).launch {
            kotlinx.coroutines.delay(1800)
            simulateReply(matchId, text)
        }
    }

    private suspend fun simulateReply(matchId: String, userText: String) = withContext(Dispatchers.IO) {
        val match = matchDao.getMatchById(matchId) ?: return@withContext
        val replies = when {
            userText.contains("coffee", ignoreCase = true) || userText.contains("drink", ignoreCase = true) || userText.contains("tea", ignoreCase = true) -> listOf(
                "I'm actually super close right now! Would love to grab a cup ☕",
                "Sounds awesome! The place around the corner has great seating.",
                "Let's do it! How does in 20 minutes sound?"
            )
            userText.contains("meet", ignoreCase = true) || userText.contains("where", ignoreCase = true) || userText.contains("spot", ignoreCase = true) -> listOf(
                "There's a really cozy spot just 400m from here with outdoor tables!",
                "I know a great place right in the middle of our 2km radius ✨",
                "Yes! Let's meet at the main plaza bench near the fountain."
            )
            userText.contains("climb", ignoreCase = true) || userText.contains("gym", ignoreCase = true) || userText.contains("run", ignoreCase = true) || userText.contains("game", ignoreCase = true) -> listOf(
                "Count me in! I'll pack my gear and head over soon 🧗",
                "That sounds so fun! Let's meet at the entrance!",
                "Great! Stoked to have a workout partner nearby."
            )
            match.mode == IntentMode.DATING -> listOf(
                "Haha I love that! What are your plans for the rest of today?",
                "That made me smile 😊 You have great taste in local spots!",
                "Totally agree! It's so nice finding people right in our neighborhood."
            )
            else -> listOf(
                "That's so cool! We should definitely hang out soon 🙌",
                "Awesome! Always looking to meet more creative folks nearby.",
                "Sounds like a plan! Let me know when you're free this week."
            )
        }
        val replyText = replies.random()
        chatMessageDao.insertMessage(
            ChatMessage(
                matchId = matchId,
                senderIsUser = false,
                text = replyText,
                timestamp = System.currentTimeMillis()
            )
        )
        matchDao.updateLastMessage(matchId, replyText, System.currentTimeMillis())
    }

    suspend fun toggleJoinHangout(hangout: HangoutSignal) = withContext(Dispatchers.IO) {
        hangoutSignalDao.toggleJoin(hangout.id, !hangout.isJoined)
    }

    suspend fun createHangout(
        title: String,
        description: String,
        locationName: String,
        mode: IntentMode,
        timeLabel: String,
        tags: List<String>,
        creatorName: String,
        creatorAvatarEmoji: String,
        creatorColor: Long
    ) = withContext(Dispatchers.IO) {
        val signal = HangoutSignal(
            title = title,
            description = description,
            locationName = locationName,
            distanceMeters = Random.nextInt(150, 1200),
            mode = mode,
            timeLabel = timeLabel,
            attendeesCount = 1,
            isJoined = true,
            creatorName = creatorName,
            creatorAvatarEmoji = creatorAvatarEmoji,
            creatorColor = creatorColor,
            tags = tags
        )
        hangoutSignalDao.insertHangout(signal)
    }

    suspend fun updateUserSettings(settings: UserProfileSettings) = withContext(Dispatchers.IO) {
        userProfileDao.saveUserSettings(settings)
    }

    suspend fun verifyPhoneNumber(phoneNumber: String) = withContext(Dispatchers.IO) {
        val current = userProfileDao.getUserSettingsDirect() ?: SeedData.getDefaultUser()
        val updated = current.copy(
            isPhoneVerified = true,
            verifiedPhoneNumber = phoneNumber,
            verificationDate = "Aug 2026"
        )
        userProfileDao.saveUserSettings(updated)
    }

    suspend fun verifySelfiePhoto(matchScore: Int) = withContext(Dispatchers.IO) {
        val current = userProfileDao.getUserSettingsDirect() ?: SeedData.getDefaultUser()
        val updated = current.copy(
            isPhotoVerified = true,
            photoMatchScore = matchScore,
            verificationDate = "Aug 2026"
        )
        userProfileDao.saveUserSettings(updated)
    }

    suspend fun resetVerification() = withContext(Dispatchers.IO) {
        val current = userProfileDao.getUserSettingsDirect() ?: SeedData.getDefaultUser()
        val updated = current.copy(
            isPhoneVerified = false,
            verifiedPhoneNumber = null,
            isPhotoVerified = false,
            photoMatchScore = 0,
            verificationDate = null
        )
        userProfileDao.saveUserSettings(updated)
    }

    suspend fun relocateTo(newLocationName: String) = withContext(Dispatchers.IO) {
        val currentSettings = userProfileDao.getUserSettingsDirect() ?: SeedData.getDefaultUser()
        userProfileDao.saveUserSettings(currentSettings.copy(currentLocationName = newLocationName))

        // Shuffle distances and angles to reflect new local neighborhood radar
        val currentProfiles = profileDao.getAllActiveProfiles().firstOrNull() ?: emptyList()
        val updated = currentProfiles.map { p ->
            val newDist = Random.nextInt(120, 1980)
            val newAngle = (Random.nextFloat() * 360f)
            p.copy(
                distanceMeters = newDist,
                distanceRatio = (newDist / 2000f).coerceIn(0.06f, 0.99f),
                angleDegrees = newAngle
            )
        }
        profileDao.insertProfiles(updated)
    }
}
