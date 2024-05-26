package com.fleaudie.amvflix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.amvflix.repositories.WatchListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(private val repository: WatchListRepository) : ViewModel() {
    private val _watchLists = MutableLiveData<List<String>>()
    val watchLists: LiveData<List<String>> get() = _watchLists

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun addWatchList(listName: String) {
        repository.addWatchList(listName) { success, message ->
            if (success) {
                getWatchLists()
            } else {
                _errorMessage.value = message
            }
        }
    }

    fun getWatchLists() {
        repository.getWatchLists { success, listNames, message ->
            if (success) {
                _watchLists.value = listNames ?: emptyList()
            } else {
                _errorMessage.value = message
            }
        }
    }


    fun removeWatchList(listName: String) {
        repository.removeWatchList(listName) { success, message ->
            if (success) {
                getWatchLists()
            } else {
                _errorMessage.value = message
            }
        }
    }

}