package com.mehmetbaloglu.learnconnect.data.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.data.models.DataOrException
import com.mehmetbaloglu.learnconnect.data.models.User
import com.mehmetbaloglu.learnconnect.data.models.VideoSavings
import com.mehmetbaloglu.learnconnect.retrofit.CoursesApi
import com.mehmetbaloglu.learnconnect.room.Dao
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class Repository @Inject constructor(
    private val auth: FirebaseAuth,
    private val coursesApi: CoursesApi,
    private val dao: Dao,
    private val firestore: FirebaseFirestore
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

    fun getCourseNames(): List<String> {
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

    // Videoyu veritabanına ekler (kaldığı saniye kayıt edilir)
    suspend fun addVideo(video: VideoSavings) {
        dao.insertVideo(video)
    }

    // Video pozisyonunu almak (kaldığı saniye çekilir)
    suspend fun getSavedVideoPosition(videoUrl: String): VideoSavings? {
        return dao.getVideoByUrl(videoUrl)
    }

    // Firestore'da favori kurs ekleme
    suspend fun addCourseToFavoritesFirestore(email: String, newCourse: String) {
        try {
            val userDocRef = firestore.collection("users").document(email)
            userDocRef.update("user_fav_courses", com.google.firebase.firestore.FieldValue.arrayUnion(newCourse))
                .await()
            Log.d("Firestore", "Favori kurs Firestore'a başarıyla eklendi.")
        } catch (e: Exception) {
            Log.e("Firestore", "Firestore'a favori kurs eklenirken hata: ${e.message}")
        }
    }

    // Firestore'da favori kurs silme
    suspend fun removeCourseFromFavoritesFirestore(email: String, courseToRemove: String) {
        try {
            val userDocRef = firestore.collection("users").document(email)
            userDocRef.update("user_fav_courses", com.google.firebase.firestore.FieldValue.arrayRemove(courseToRemove))
                .await()
            Log.d("Firestore", "Favori kurs Firestore'dan başarıyla silindi.")
        } catch (e: Exception) {
            Log.e("Firestore", "Firestore'dan favori kurs silinirken hata: ${e.message}")
        }
    }

    // Firestore'da kayıtlı kurs ekleme
    suspend fun addCourseToRegisteredCoursesFirestore(email: String, newCourse: String) {
        try {
            val userDocRef = firestore.collection("users").document(email)
            userDocRef.update("user_registered_courses", com.google.firebase.firestore.FieldValue.arrayUnion(newCourse))
                .await()
            Log.d("Firestore", "Kayıtlı kurs Firestore'a başarıyla eklendi.")
        } catch (e: Exception) {
            Log.e("Firestore", "Firestore'a kayıtlı kurs eklenirken hata: ${e.message}")
        }
    }

    // Firestore'da kayıtlı kurs silme
    suspend fun removeCourseFromRegisteredCoursesFirestore(email: String, courseToRemove: String) {
        try {
            val userDocRef = firestore.collection("users").document(email)
            userDocRef.update("user_registered_courses", com.google.firebase.firestore.FieldValue.arrayRemove(courseToRemove))
                .await()
            Log.d("Firestore", "Kayıtlı kurs Firestore'dan başarıyla silindi.")
        } catch (e: Exception) {
            Log.e("Firestore", "Firestore'dan kayıtlı kurs silinirken hata: ${e.message}")
        }
    }

    // Firestore'a yeni kullanıcı ekle
    suspend fun addUserToFirestore(user: User) {
        try {
            val userRef = user.user_email?.let { firestore.collection("users").document(it) }
            userRef?.set(user)?.await()  // Firestore'a kullanıcıyı ekle
        } catch (e: Exception) {
            throw e
        }
    }

    // Firestore'dan kullanıcıyı kontrol et (var mı yok mu)
    suspend fun getUserFromFirestore(email: String): User? {
        return try {
            val userRef = firestore.collection("users").document(email).get().await()
            if (userRef.exists()) {
                userRef.toObject(User::class.java) // Firestore'dan kullanıcıyı döndür
            } else {
                null
            }
        } catch (e: Exception) {
            throw e
        }
    }

    //video açılmadan önce kursa kayıt yapılıp yapılmadığını kontrol et
    suspend fun isCourseRegisteredInFirestore(email: String, courseName: String): Boolean {
        return try {
            // Kullanıcının Firestore'daki verisini al
            val userDocRef = firestore.collection("users").document(email).get().await()

            // Eğer kullanıcı varsa, kayıtlı kursları kontrol et
            if (userDocRef.exists()) {
                val user = userDocRef.toObject(User::class.java)
                val registeredCourses = user?.user_registered_courses // Kullanıcının kayıtlı kursları

                // Eğer kayıtlı kurslar listesi varsa ve kurs adı listede yer alıyorsa true döndür
                return registeredCourses?.contains(courseName) == true
            } else {
                false
            }
        } catch (e: Exception) {
            // Hata durumunda false döndür
            Log.e("Firestore", "Kurs kontrolü yapılırken hata: ${e.message}")
            false
        }
    }

    // Firestore'dan kullanıcının kayıtlı kurslarını al
    suspend fun getUserRegisteredCoursesFromFirestore(email: String): List<String>? {
        return try {
            // Kullanıcının Firestore'daki verisini al
            val userDocRef = firestore.collection("users").document(email).get().await()

            // Eğer kullanıcı varsa, kayıtlı kursları döndür
            if (userDocRef.exists()) {
                val user = userDocRef.toObject(User::class.java)
                user?.user_registered_courses // Kullanıcının kayıtlı kursları
            } else {
                null // Kullanıcı yoksa null döndür
            }
        } catch (e: Exception) {
            // Hata durumunda null döndür
            Log.e("Firestore", "Kayıtlı kurslar alınırken hata: ${e.message}")
            null
        }
    }

    // Firestore'dan kullanıcının favori kurslarını al
    suspend fun getUserFavCoursesFromFirestore(email: String): List<String>? {
        return try {
            // Kullanıcının Firestore'daki verisini al
            val userDocRef = firestore.collection("users").document(email).get().await()

            // Eğer kullanıcı varsa, favori kursları döndür
            if (userDocRef.exists()) {
                val user = userDocRef.toObject(User::class.java)
                user?.user_fav_courses // Kullanıcının favori kursları
            } else {
                null // Kullanıcı yoksa null döndür
            }
        } catch (e: Exception) {
            // Hata durumunda null döndür
            Log.e("Firestore", "Favori kurslar alınırken hata: ${e.message}")
            null
        }
    }

}