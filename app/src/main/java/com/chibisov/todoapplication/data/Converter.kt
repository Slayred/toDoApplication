package com.chibisov.todoapplication.data

import androidx.room.TypeConverter
import com.chibisov.todoapplication.data.model.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}