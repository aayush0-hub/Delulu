package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChatMessage
import com.example.data.model.HangoutSignal
import com.example.data.model.Match
import com.example.data.model.Profile
import com.example.data.model.UserProfileSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles WHERE isPassed = 0 ORDER BY distanceMeters ASC")
    fun getAllActiveProfiles(): Flow<List<Profile>>

    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getProfileById(id: String): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<Profile>)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("UPDATE profiles SET isLiked = 1, isMatched = :isMatch WHERE id = :id")
    suspend fun likeProfile(id: String, isMatch: Boolean)

    @Query("UPDATE profiles SET isPassed = 1 WHERE id = :id")
    suspend fun passProfile(id: String)

    @Query("UPDATE profiles SET isPassed = 0, isLiked = 0, isMatched = 0")
    suspend fun resetProfiles()

    @Query("SELECT COUNT(*) FROM profiles")
    suspend fun countProfiles(): Int
}

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY lastMessageTimestamp DESC")
    fun getAllMatches(): Flow<List<Match>>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getMatchById(id: String): Match?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match)

    @Query("UPDATE matches SET lastMessage = :lastMsg, lastMessageTimestamp = :time, unreadCount = unreadCount + 1 WHERE id = :id")
    suspend fun updateLastMessage(id: String, lastMsg: String, time: Long)

    @Query("UPDATE matches SET unreadCount = 0 WHERE id = :id")
    suspend fun clearUnread(id: String)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun deleteMatch(id: String)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE matchId = :matchId ORDER BY timestamp ASC")
    fun getMessagesForMatch(matchId: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages WHERE matchId = :matchId")
    suspend fun deleteMessagesForMatch(matchId: String)
}

@Dao
interface HangoutSignalDao {
    @Query("SELECT * FROM hangout_signals ORDER BY distanceMeters ASC")
    fun getAllHangouts(): Flow<List<HangoutSignal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHangout(hangout: HangoutSignal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHangouts(hangouts: List<HangoutSignal>)

    @Query("UPDATE hangout_signals SET isJoined = :isJoined, attendeesCount = attendeesCount + (CASE WHEN :isJoined THEN 1 ELSE -1 END) WHERE id = :id")
    suspend fun toggleJoin(id: String, isJoined: Boolean)

    @Query("SELECT COUNT(*) FROM hangout_signals")
    suspend fun countHangouts(): Int
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getUserSettings(): Flow<UserProfileSettings?>

    @Query("SELECT * FROM user_settings WHERE id = 1")
    suspend fun getUserSettingsDirect(): UserProfileSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSettings(settings: UserProfileSettings)
}
