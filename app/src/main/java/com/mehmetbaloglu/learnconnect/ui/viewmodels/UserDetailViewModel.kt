package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {


    private val _userRegisteredCourses = MutableStateFlow<List<String>?>(null)
    val userRegisteredCourses = _userRegisteredCourses.asStateFlow()

    private val _userFavCourses = MutableStateFlow<List<String>?>(null)
    val userFavCourses= _userFavCourses.asStateFlow()

    // Get the list of registered courses for a user
    fun getUserRegisteredCourses(email: String) {
        viewModelScope.launch {
            try {
                val courses = repository.getUserRegisteredCoursesFromFirestore(email)
                _userRegisteredCourses.value = courses
            } catch (e: Exception) {
                // Handle error, possibly set value to null or show a message
                _userRegisteredCourses.value = null
            }
        }
    }

    // Get the list of favorite courses for a user
    fun getUserFavCourses(email: String) {
        viewModelScope.launch {
            try {
                val courses = repository.getUserFavCoursesFromFirestore(email)
                _userFavCourses.value = courses
            } catch (e: Exception) {
                // Handle error, possibly set value to null or show a message
                _userFavCourses.value = null
            }
        }
    }

    // Remove a course from registered courses
    fun removeCourseFromRegisteredCourses(email: String, courseName: String) {
        viewModelScope.launch {
            try {
                repository.removeCourseFromRegisteredCoursesFirestore(email, courseName)
                getUserRegisteredCourses(email) // Re-fetch to update the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }


    // Remove a course from favorite courses
    fun removeCourseFromFavorites(email: String, courseName: String) {
        viewModelScope.launch {
            try {
                repository.removeCourseFromFavoritesFirestore(email, courseName)
                getUserFavCourses(email) // Re-fetch to update the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }




}