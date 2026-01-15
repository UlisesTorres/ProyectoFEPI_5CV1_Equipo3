package com.example.myapplication.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TConvert {

    private val gson = Gson()

    @TypeConverter
    fun fromIntList(list: List<Int>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIntList(data: String?): List<Int> {
        if (data.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(data, listType)
    }
}
