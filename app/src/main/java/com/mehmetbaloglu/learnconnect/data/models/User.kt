package com.mehmetbaloglu.learnconnect.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mehmetbaloglu.learnconnect.utils.CourseTypeConverter


@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)  // Benzersiz bir id oluşturuyoruz
    val id: Long? = null,  // Varsayılan olarak sıfır olacak, Room tarafından otomatik artırılacak

    val user_email: String?=null,

    @TypeConverters(CourseTypeConverter::class)  // CourseTypeConverter ile List<String> alanı dönüştürülüyor
    val user_fav_courses: List<String>? = null,

    @TypeConverters(CourseTypeConverter::class)  // CourseTypeConverter ile String alanı dönüştürülüyor
    val user_registered_courses: List<String>? = null
)
