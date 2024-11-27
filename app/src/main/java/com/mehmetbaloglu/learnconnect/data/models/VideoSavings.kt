package com.mehmetbaloglu.learnconnect.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "video_savings")
data class VideoSavings(
    @PrimaryKey
    val video_url:String,
    val video_last_saved_time:Long,
    val user_mail: String,
)
