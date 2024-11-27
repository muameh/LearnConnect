package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.data.models.DataOrException
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    private val _addFavState = MutableStateFlow<String?>(null)
    val actionState: StateFlow<String?> = _addFavState.asStateFlow()

    //network request
    suspend fun getCourseDetails(courseName: String): DataOrException<CourseDetails, Boolean, Exception> {
        return repository.getCourseDetails(courseName)
    }


    // Firestore'da kursu favorilere ekler
    fun addCourseToFavoritesFirestore(email: String, newCourse: String) =
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addCourseToFavoritesFirestore(email, newCourse)
                _addFavState.value = "added Successfully"
            }
        } catch (e: Exception){
            _addFavState.value = "${e.message}"
        }


    // Firestore'da kursu kayıtlı kurslara ekler
    fun addCourseToRegisteredCoursesFirestore(email: String, newCourse: String) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCourseToRegisteredCoursesFirestore(email, newCourse)
        }

    // Check if the course is registered in Firestore
    fun isCourseRegisteredInFirestore(email: String, courseName: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val isRegistered = repository.isCourseRegisteredInFirestore(email, courseName)
            onResult(isRegistered)
        }
    }




}