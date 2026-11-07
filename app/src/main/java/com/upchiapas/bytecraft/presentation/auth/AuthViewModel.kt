package com.upchiapas.bytecraft.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = null, generalError = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChange(confirm: String) {
        _uiState.update { it.copy(confirmPassword = confirm, confirmPasswordError = null, generalError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun login() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        var hasError = false
        var emailErr: String? = null
        var passErr: String? = null

        if (email.isBlank()) {
            emailErr = "El correo es obligatorio"
            hasError = true
        } else if (!email.contains("@") || !email.contains(".")) {
            emailErr = "Formato de correo no válido"
            hasError = true
        }

        if (password.isBlank()) {
            passErr = "La contraseña es obligatoria"
            hasError = true
        } else if (password.length < 8) {
            passErr = "La contraseña debe tener al menos 8 caracteres"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    emailError = emailErr,
                    passwordError = passErr,
                    generalError = null
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        // Simulación de autenticación (En Commit 4 y 5 se conecta a Retrofit API)
        viewModelScope.launch {
            delay(500)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoginSuccess = true,
                    generalError = null
                )
            }
        }
    }

    fun register() {
        val name = _uiState.value.name.trim()
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirm = _uiState.value.confirmPassword

        var hasError = false
        var nameErr: String? = null
        var emailErr: String? = null
        var passErr: String? = null
        var confirmErr: String? = null

        if (name.isBlank() || name.length < 3) {
            nameErr = "El nombre debe tener al menos 3 letras"
            hasError = true
        }

        if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
            emailErr = "Ingresa un correo electrónico válido"
            hasError = true
        }

        if (password.length < 8) {
            passErr = "La contraseña debe tener mínimo 8 caracteres"
            hasError = true
        }

        if (password != confirm) {
            confirmErr = "Las contraseñas no coinciden"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    emailError = emailErr,
                    passwordError = passErr,
                    confirmPasswordError = confirmErr
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        // Simulación de registro
        viewModelScope.launch {
            delay(500)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRegisterSuccess = true
                )
            }
        }
    }

    fun resetSuccessFlags() {
        _uiState.update {
            it.copy(
                isLoginSuccess = false,
                isRegisterSuccess = false
            )
        }
    }
}
