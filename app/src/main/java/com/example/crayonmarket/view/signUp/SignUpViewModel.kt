package com.example.crayonmarket.view.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crayonmarket.firebase.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateConfirmPassword(confirmPassWord: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassWord) }
    }

    fun signUp() {
        val email = uiState.value.email
        val password = uiState.value.password
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = AuthRepository.signUp(email, password)
            if (result.isSuccess) {
                _uiState.update { it.copy(successToSignUp = true, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = result.exceptionOrNull()!!.localizedMessage,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}