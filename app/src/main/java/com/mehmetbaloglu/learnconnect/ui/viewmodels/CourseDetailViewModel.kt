package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.data.models.DataOrException
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    //network request
    suspend fun getCourseDetails(
        courseName: String
    ): DataOrException<CourseDetails, Boolean, Exception> {
        return repository.getCourseDetails(courseName)
    }


}