package com.daleen.freelanceflow.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.AuthState
import com.daleen.freelanceflow.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val authState: StateFlow<AuthState> = authRepository.authState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AuthState.Loading)

    private val _formState = MutableStateFlow(AuthFormState())
    val formState: StateFlow<AuthFormState> = _formState

    fun updateEmail(value: String) {
        _formState.update { it.copy(email = value, error = null) }
    }

    fun updatePassword(value: String) {
        _formState.update { it.copy(password = value, error = null) }
    }

    fun toggleMode() {
        _formState.update { it.copy(isSignUp = !it.isSignUp, error = null) }
    }

    fun submit() {
        val form = _formState.value
        if (form.email.isBlank() || form.password.isBlank()) {
            _formState.update { it.copy(error = "Email and password are required") }
            return
        }
        _formState.update { it.copy(isSubmitting = true, error = null) }
        viewModelScope.launch {
            val result = if (form.isSignUp) {
                authRepository.signUp(form.email.trim(), form.password)
            } else {
                authRepository.signIn(form.email.trim(), form.password)
            }
            result
                .onSuccess { _formState.update { it.copy(isSubmitting = false) } }
                .onFailure { throwable ->
                    _formState.update {
                        it.copy(isSubmitting = false, error = throwable.message ?: "Something went wrong")
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch { authRepository.signOut() }
    }
}

data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val isSignUp: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
)
