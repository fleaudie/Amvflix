package com.fleaudie.amvflix.data.datasource

import android.util.Log
import com.fleaudie.amvflix.data.model.ListContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class WatchListDataSource {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun addWatchList(listName: String, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val watchListData = hashMapOf(
                "listName" to listName,
                "createdAt" to FieldValue.serverTimestamp()
            )
            firestore.collection("users").document(user.uid).collection("watchLists")
                .add(watchListData)
                .addOnSuccessListener {
                    onComplete(true, null)
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } ?: run {
            onComplete(false, "User not logged in")
        }
    }

    fun getWatchLists(onComplete: (Boolean, List<String>?, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            firestore.collection("users").document(user.uid).collection("watchLists")
                .orderBy("listName")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val listNames = querySnapshot.documents.map { it.getString("listName") ?: "" }
                    onComplete(true, listNames, null)
                }
                .addOnFailureListener { e ->
                    onComplete(false, null, e.message)
                }
        } ?: run {
            onComplete(false, null, "User not logged in")
        }
    }

    fun removeWatchList(listName: String, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val watchListRef = firestore.collection("users").document(user.uid).collection("watchLists")
            watchListRef.whereEqualTo("listName", listName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        watchListRef.document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                onComplete(true, null)
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    } else {
                        onComplete(false, "List not found!")
                    }
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } ?: run {
            onComplete(false, "User not logged in!")
        }
    }

    fun addAnimeToList(animeName: String, animeLogo: String, listName: String, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val watchListRef = firestore.collection("users").document(user.uid).collection("watchLists")

            watchListRef.whereEqualTo("listName", listName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val listId = document.id
                        val animeData = hashMapOf(
                            "animeName" to animeName,
                            "animeLogo" to animeLogo,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                        firestore.collection("users").document(user.uid).collection("watchLists").document(listId)
                            .collection("animeList")
                            .add(animeData)
                            .addOnSuccessListener {
                                onComplete(true, null)
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    } else {
                        val watchListData = hashMapOf(
                            "listName" to listName,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                        firestore.collection("users").document(user.uid).collection("watchLists")
                            .add(watchListData)
                            .addOnSuccessListener { documentReference ->
                                val animeData = hashMapOf(
                                    "animeName" to animeName,
                                    "animeLogo" to animeLogo,
                                    "createdAt" to FieldValue.serverTimestamp()
                                )
                                firestore.collection("users").document(user.uid).collection("watchLists")
                                    .document(documentReference.id)
                                    .collection("animeList")
                                    .add(animeData)
                                    .addOnSuccessListener {
                                        onComplete(true, null)
                                    }
                                    .addOnFailureListener { e ->
                                        onComplete(false, e.message)
                                    }
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } ?: run {
            onComplete(false, "User not logged in")
        }
    }

    fun getAnimeList(listName: String, onComplete: (Boolean, List<ListContent>?, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val animeList = mutableListOf<ListContent>()
            val watchListRef = firestore.collection("users").document(user.uid).collection("watchLists")

            watchListRef.whereEqualTo("listName", listName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val listId = document.id
                        firestore.collection("users").document(user.uid)
                            .collection("watchLists").document(listId)
                            .collection("animeList")
                            .get()
                            .addOnSuccessListener { animeQuerySnapshot ->
                                for (animeDocument in animeQuerySnapshot.documents) {
                                    val animeName = animeDocument.getString("animeName") ?: ""
                                    val animeLogo = animeDocument.getString("animeLogo") ?: ""
                                    animeList.add(ListContent(animeName, animeLogo))
                                }
                                onComplete(true, animeList, null)
                            }
                            .addOnFailureListener { e ->
                                Log.e("getAnimeList", "Error getting anime list: ", e)
                                onComplete(false, null, e.message)
                            }
                    } else {
                        Log.d("getAnimeList", "List with name $listName not found")
                        onComplete(true, animeList, null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("getAnimeList", "Error getting watch lists: ", e)
                    onComplete(false, null, e.message)
                }
        } ?: run {
            onComplete(false, null, "User not logged in")
        }
    }

    fun removeAnimeFromList(listName: String, anime: ListContent, onComplete: (Boolean, String?) -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val watchListRef = firestore.collection("users").document(user.uid).collection("watchLists")

            watchListRef.whereEqualTo("listName", listName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val listId = document.id

                        firestore.collection("users").document(user.uid)
                            .collection("watchLists").document(listId)
                            .collection("animeList")
                            .whereEqualTo("animeName", anime.animeName)
                            .get()
                            .addOnSuccessListener { animeSnapshot ->
                                if (!animeSnapshot.isEmpty) {
                                    val animeDocument = animeSnapshot.documents[0]
                                    animeDocument.reference.delete()
                                        .addOnSuccessListener {
                                            onComplete(true, null)
                                        }
                                        .addOnFailureListener { e ->
                                            onComplete(false, e.message)
                                        }
                                } else {
                                    onComplete(false, "Anime not found in list")
                                }
                            }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                    } else {
                        onComplete(false, "List not found")
                    }
                }
                .addOnFailureListener { e ->
                    onComplete(false, e.message)
                }
        } ?: run {
            onComplete(false, "User not logged in")
        }
    }

}