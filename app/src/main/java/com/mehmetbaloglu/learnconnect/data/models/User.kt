package com.mehmetbaloglu.learnconnect.data.models

import androidx.room.Entity


@Entity(tableName = "users")
data class User(
    val user_email:String?,
    val user_fav_courses:List<Video>? = null,
    val user_registered_courses:List<Video>? = null
)
