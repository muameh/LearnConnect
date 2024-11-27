package com.mehmetbaloglu.learnconnect.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mehmetbaloglu.learnconnect.data.models.User
import com.mehmetbaloglu.learnconnect.data.models.VideoSavings
import com.mehmetbaloglu.learnconnect.utils.CourseTypeConverter

@Database(entities = [User::class, VideoSavings::class], version = 1, exportSchema = false)
@TypeConverters(CourseTypeConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun Dao(): Dao

}