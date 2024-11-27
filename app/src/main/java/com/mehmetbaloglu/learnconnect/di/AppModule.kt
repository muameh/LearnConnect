package com.mehmetbaloglu.learnconnect.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mehmetbaloglu.learnconnect.retrofit.CoursesApi
import com.mehmetbaloglu.learnconnect.room.Dao
import com.mehmetbaloglu.learnconnect.room.DataBase
import com.mehmetbaloglu.learnconnect.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideCoursesApi(): CoursesApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoursesApi::class.java)
    }

    @Provides
    @Singleton
    fun dataBase(@ApplicationContext context: Context): DataBase =
        Room
            .databaseBuilder(
                context,
                DataBase::class.java,
                "learnConnect_db"
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideWeatherDao(dataBase: DataBase): Dao = dataBase.Dao()





}