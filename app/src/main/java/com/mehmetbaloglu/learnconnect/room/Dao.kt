package com.mehmetbaloglu.learnconnect.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mehmetbaloglu.learnconnect.data.models.User
import com.mehmetbaloglu.learnconnect.data.models.VideoSavings

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE user_email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoSavings)

    @Query("SELECT * FROM video_savings WHERE video_url = :videoUrl LIMIT 1")
    suspend fun getVideoByUrl(videoUrl: String): VideoSavings?







}