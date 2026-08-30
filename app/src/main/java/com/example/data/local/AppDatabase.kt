package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.ChatMessage
import com.example.data.model.HangoutSignal
import com.example.data.model.Match
import com.example.data.model.Profile
import com.example.data.model.UserProfileSettings

@Database(
    entities = [
        Profile::class,
        Match::class,
        ChatMessage::class,
        HangoutSignal::class,
        UserProfileSettings::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun matchDao(): MatchDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun hangoutSignalDao(): HangoutSignalDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "twokm_radar_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
        }

        fun getDatabase(context: Context? = null): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null && context != null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "twokm_radar_database"
                    ).fallbackToDestructiveMigration().build()
                }
                INSTANCE ?: throw IllegalStateException("AppDatabase must be initialized first")
            }
        }
    }
}
