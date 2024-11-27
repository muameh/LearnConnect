package com.mehmetbaloglu.learnconnect.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CourseTypeConverter {

    private val gson = Gson()  // Gson nesnesini bir kere oluşturuyoruz

    // List<String> verisini JSON string'e dönüştür
    @TypeConverter
    fun fromCourseList(courseList: List<String>?): String? {
        return courseList?.let { Gson().toJson(it) }
    }

    // JSON string verisini List<String> türüne dönüştür
    @TypeConverter
    fun toCourseList(coursesJson: String?): List<String>? {
        return coursesJson?.let {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson<List<String>>(it, type)
        }
    }

}