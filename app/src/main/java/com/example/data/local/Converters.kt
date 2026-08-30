package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.IntentMode

class Converters {
    @TypeConverter
    fun fromIntentMode(mode: IntentMode): String {
        return mode.name
    }

    @TypeConverter
    fun toIntentMode(value: String): IntentMode {
        return try {
            IntentMode.valueOf(value)
        } catch (e: Exception) {
            IntentMode.BOTH
        }
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(";;;")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split(";;;")
    }
}
