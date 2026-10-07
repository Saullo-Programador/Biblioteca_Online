package com.example.biblioteca.data.local.converter

import androidx.room.TypeConverter
import com.example.biblioteca.domain.model.ReadingStatus

class RoomConverters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = "|||")
    }

    @TypeConverter
    fun toStringList(value: String): List<String>{
        return if(value.isNullOrBlank()){
            emptyList()
        } else {
            value.split("|||")
        }
    }

    @TypeConverter
    fun fromReadingStatus(status: ReadingStatus): String{
        return status.name
    }

    @TypeConverter
    fun toReadingStatus(value: String): ReadingStatus{
        return try {
            ReadingStatus.valueOf(value)
        } catch (exception: IllegalArgumentException){
            ReadingStatus.WANT_TO_READ
        }
    }
}