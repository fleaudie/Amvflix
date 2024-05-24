package com.fleaudie.amvflix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.amvflix.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepo : AuthRepository) : ViewModel() {

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?>
        get() = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?>
        get() = _passwordError

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        if (email.isEmpty()) {
            _emailError.value = "Please enter your email"
            return
        } else {
            _emailError.value = null
        }
        if (password.isEmpty()) {
            _passwordError.value = "Please enter your password"
            return
        } else {
            _passwordError.value = null
        }

        authRepo.login(email, password, onComplete)
    }

    fun checkUsername(onSuccess: (Boolean) -> Unit) =
        authRepo.checkUsername(onSuccess)
}