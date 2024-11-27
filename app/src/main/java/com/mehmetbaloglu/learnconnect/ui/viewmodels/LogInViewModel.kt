package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmetbaloglu.learnconnect.data.models.LogInState
import com.mehmetbaloglu.learnconnect.data.models.User
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()


    fun LogIn(email: String, password: String) {
        _logInState.value = LogInState.Loading
        viewModelScope.launch {
            val result = repository.logIn(email, password)
            result.fold(
                onSuccess = {
                    val displayName = it.user?.displayName ?: "User"
                    _logInState.value = LogInState.Success
                    _logInMessage.value = "Welcome, $displayName"
                    checkAndAddUserToFirestore(email)
                },
                onFailure = { error ->
                    _logInState.value = LogInState.Error
                    _logInMessage.value = error.localizedMessage ?: "Unknown error occurred"
                }
            )
        }
    }

    // Firestore'a kullanıcı ekleyip kontrol etme fonksiyonu
    fun checkAndAddUserToFirestore(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Firestore'dan kullanıcıyı al
                val existingUser = repository.getUserFromFirestore(email)

                if (existingUser == null) {
                    // Kullanıcı yoksa, yeni bir kullanıcı oluştur
                    val newUser = User(user_email = email)
                    // Firestore'a kullanıcıyı ekle
                    repository.addUserToFirestore(newUser)
                    _logInMessage.value = "User created successfully for $email"
                } else {
                    _logInMessage.value = "User already exists in Firestore."
                }
            } catch (e: Exception) {
                // Hata durumunu işle
                _logInMessage.value = "Error adding user: ${e.localizedMessage}"
            }
        }
    }

}