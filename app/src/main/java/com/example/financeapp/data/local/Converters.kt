package com.example.financeapp.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toCategory(value: String) = enumValueOf<CategoryType>(value)

    @TypeConverter
    fun fromCategory(value: CategoryType) = value.name
}