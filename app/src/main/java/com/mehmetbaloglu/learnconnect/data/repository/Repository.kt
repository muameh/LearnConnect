package com.mehmetbaloglu.learnconnect.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.data.models.DataOrException
import com.mehmetbaloglu.learnconnect.retrofit.CoursesApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class Repository @Inject constructor(
    private val auth: FirebaseAuth,
    private val coursesApi: CoursesApi
) {

    suspend fun logIn(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourseDetails(courseName: String)
            : DataOrException<CourseDetails, Boolean, Exception> {
        return try {
            DataOrException(data = coursesApi.getVideos(courseName=courseName))
        } catch (e: Exception) {
            DataOrException(e = e)
        }

    }

    suspend fun signUpWithEmail(
        userName: String,
        email: String,
        password: String
    ): Result<AuthResult> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(userName).build()
            )?.await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourseCategories(): List<String> {
        return listOf(
            "fashion",
            "nature",
            "science",
            "education",
            "feelings",
            "health",
            "people",
            "religion",
            "places",
            "animals",
            "industry",
            "computer",
            "food",
            "sports",
            "transportation",
            "travel",
            "buildings",
            "business",
            "music"
        )

    }


}