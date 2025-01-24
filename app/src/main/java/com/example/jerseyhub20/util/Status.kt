package com.example.jerseyhub20.util

//
sealed class Status {
    data object Authenticated : Status()
    data object NotAuthenticated : Status()
    data object Loading : Status()
    data class Error(val message: String) : Status()
}

//