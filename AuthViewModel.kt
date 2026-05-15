package com.ksheera.sagara.ui.screens

import androidx.lifecycle.ViewModel
import com.ksheera.sagara.data.local.prefs.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {

    private val _isUserRegistered = MutableStateFlow(userManager.isUserRegistered())
    val isUserRegistered: StateFlow<Boolean> = _isUserRegistered.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    fun clearError() {
        _authError.value = null
    }

    fun login(username: String, password: String): Boolean {
        return if (userManager.loginUser(username, password)) {
            true
        } else {
            _authError.value = "Invalid username or password"
            false
        }
    }

    fun signup(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) {
            _authError.value = "Username and password cannot be empty"
            return false
        }
        return if (userManager.registerUser(username, password)) {
            _isUserRegistered.value = true
            true
        } else {
            _authError.value = "Username already exists. Please choose another or login."
            false
        }
    }
}
