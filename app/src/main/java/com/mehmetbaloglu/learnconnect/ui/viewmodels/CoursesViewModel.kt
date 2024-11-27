package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val repository: Repository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _courseCategories = MutableStateFlow<List<String>>(emptyList())
    val courseCategories: StateFlow<List<String>> = _courseCategories.asStateFlow()

    init {
        getCourseCategories()
    }

    private fun getCourseCategories() {
        viewModelScope.launch {
            _courseCategories.value = repository.getCourseNames()
        }
    }








}