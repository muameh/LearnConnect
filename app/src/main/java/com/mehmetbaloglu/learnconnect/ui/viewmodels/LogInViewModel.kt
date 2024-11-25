package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmetbaloglu.learnconnect.data.models.LogInState
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _logInState = MutableStateFlow<LogInState>(LogInState.Nothing)
    val logInState: StateFlow<LogInState> = _logInState.asStateFlow()

    private val _logInMessage = MutableStateFlow<String?>(null)
    val logInMessage: StateFlow<String?> = _logInMessage.asStateFlow()

    fun LogIn(email: String, password: String) {
        _logInState.value = LogInState.Loading
        viewModelScope.launch {
            val result = repository.logIn(email, password)
            result.fold(
                onSuccess = {
                    val displayName = it.user?.displayName ?: "User"
                    _logInState.value = LogInState.Success
                    _logInMessage.value = "Welcome, $displayName"
                },
                onFailure = { error ->
                    _logInState.value = LogInState.Error
                    _logInMessage.value = error.localizedMessage ?: "Unknown error occurred"
                }
            )
        }
    }


}