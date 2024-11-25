package com.mehmetbaloglu.learnconnect.ui.viewmodels

import android.util.Log
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
class SignUpViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _signUpState = MutableStateFlow<LogInState>(LogInState.Nothing)
    val signUpState: StateFlow<LogInState> = _signUpState.asStateFlow()

    private val _signUpMessage = MutableStateFlow<String?>(null)
    val signUpMessage: StateFlow<String?> = _signUpMessage.asStateFlow()

    fun signUpWithEmail(userName: String, email: String, password: String) {
        _signUpState.value = LogInState.Loading

        viewModelScope.launch {
            val result = repository.signUpWithEmail(userName, email, password)
            result.fold(
                onSuccess = {
                    _signUpState.value = LogInState.Success
                    _signUpMessage.value = "User signed up successfully."
                },
                onFailure = { error ->
                    _signUpState.value = LogInState.Error
                    _signUpMessage.value = error.localizedMessage
                    Log.d("signUpError", error.toString())
                }
            )
        }
    }

}