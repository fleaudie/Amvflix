package com.fleaudie.amvflix.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class AuthDataSource {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun register(
        email: String,
        password: String,
        name: String,
        surname: String,
        onComplete: (Boolean, String?, Boolean) -> Unit
    ) {
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result?.signInMethods?.isEmpty() ?: true
                if (!result) {
                    onComplete(false, "This email is already in use.", true)
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                val userData = hashMapOf(
                                    "name" to name,
                                    "surname" to surname,
                                    "email" to email
                                )
                                user?.let {
                                    firestore.collection("users").document(user.uid)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            user.sendEmailVerification()
                                                .addOnCompleteListener { verificationTask ->
                                                    if (verificationTask.isSuccessful) {
                                                        onComplete(true, null, false)
                                                    } else {
                                                        onComplete(false, verificationTask.exception?.message, false)
                                                    }
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            onComplete(false, e.message, false)
                                        }
                                }
                            } else {
                                val errorMessage = task.exception?.message
                                onComplete(false, errorMessage, false)
                            }
                        }
                        .addOnFailureListener { exception ->
                            if (exception is FirebaseAuthUserCollisionException) {
                                onComplete(false, "This email is already in use.", true)
                            } else {
                                onComplete(false, exception.message, false)
                            }
                        }
                }
            } else {
                onComplete(false, task.exception?.message, false)
            }
        }
    }

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    val errorMessage = task.exception?.message
                    onComplete(false, errorMessage)
                }
            }
    }

    fun createUsername(username: String, onComplete: (Boolean, String?) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    onComplete(false, "This username is already in use.")
                } else {
                    val currentUser = firebaseAuth.currentUser
                    currentUser?.let { user ->
                        val userData = hashMapOf(
                            "username" to username
                        ) as Map<String, Any>
                        firestore.collection("users").document(user.uid)
                            .update(userData)
                            .addOnSuccessListener {
                                onComplete(true, null)
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }

    fun checkUsername(onSuccess: (Boolean) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("username")
                        onSuccess(!username.isNullOrEmpty())
                    }
                }
                .addOnFailureListener {
                    onSuccess(false)
                }
        }
    }
}

