package com.fleaudie.amvflix.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {

    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?>
        get() = _nameError

    private val _surnameError = MutableLiveData<String?>()
    val surnameError: LiveData<String?>
        get() = _surnameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?>
        get() = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?>
        get() = _passwordError

    private val _passwordControlError = MutableLiveData<String?>()
    val passwordControlError: LiveData<String?>
        get() = _passwordControlError

    fun register(
        view: View,
        email: String,
        password: String,
        name: String,
        surname: String,
        rePassword: String
    ) {
        if (name.isEmpty()) {
            _nameError.value = "Please enter your name"
            return
        } else {
            _nameError.value = null
        }

        if (surname.isEmpty()) {
            _surnameError.value = "Please enter your surname"
            return
        } else {
            _surnameError.value = null
        }

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

        if (rePassword.isEmpty()) {
            _passwordControlError.value = "Please enter your password again"
            return
        } else {
            _passwordControlError.value = null
        }

        if (password != rePassword) {
            _passwordControlError.value = "Passwords do not match"
            return
        } else {
            _passwordControlError.value = null
        }

        authRepo.register(email, password, name, surname) { isSuccess, _, emailInUse ->
            if (isSuccess) {
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment)
            } else {
                if (emailInUse) {
                    _emailError.value = "This email is already in use."
                } else {
                    // failed
                }
            }
        }
    }
}
