package com.mehmetbaloglu.learnconnect.data.models

sealed class LogInState {
    object Nothing : LogInState()
    object Loading : LogInState()
    object Success : LogInState()
    object Error : LogInState()
}