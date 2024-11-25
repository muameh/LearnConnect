package com.mehmetbaloglu.learnconnect.retrofit

import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface CoursesApi {

    @GET("videos/")
    suspend fun getVideos(
        @Query("key") apiKey: String = Constants.API_KEY,  // API anahtarı
        @Query("q") courseName: String,                         // Dinamik arama terimi
        @Query("video_type") videoType: String = "film"     // Video türü, sabit olarak "film"
    ): CourseDetails

}