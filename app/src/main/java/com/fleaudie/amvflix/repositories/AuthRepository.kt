package com.fleaudie.amvflix.repositories

import com.fleaudie.amvflix.data.datasource.AuthDataSource

class AuthRepository(private val ads: AuthDataSource) {
    fun register(
        email: String,
        password: String,
        name: String,
        surname: String,
        onComplete: (Boolean, String?, Boolean) -> Unit
    ) =
        ads.register(email, password, name, surname, onComplete)

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) =
        ads.login(email, password, onComplete)

    fun createUsername(username: String, onComplete: (Boolean, String?) -> Unit) =
        ads.createUsername(username, onComplete)

    fun checkUsername(onSuccess: (Boolean) -> Unit) =
        ads.checkUsername(onSuccess)
}