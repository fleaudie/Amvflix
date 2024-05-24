package com.fleaudie.amvflix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.amvflix.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {

    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?>
        get() = _usernameError

    fun createUsername(username: String, onComplete: (Boolean, String?) -> Unit) {
        if (username.isEmpty()) {
            _usernameError.value = "Please enter your username"
            return
        } else {
            _usernameError.value = null
        }

        authRepo.createUsername(username) { isSuccess, errorMessage ->
            if (!isSuccess) {
                _usernameError.value = "Username already exist."
            }
            onComplete(isSuccess, errorMessage)
        }
    }
}