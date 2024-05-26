package com.fleaudie.amvflix.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleaudie.amvflix.data.model.AnimeList
import com.fleaudie.amvflix.repositories.AnimeListRepository
import com.fleaudie.amvflix.repositories.WatchListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(private var repository: AnimeListRepository, private val watchRepository: WatchListRepository) : ViewModel(){

    private val _animeDetail = MutableLiveData<AnimeList>()
    val animeDetail: LiveData<AnimeList>
        get() = _animeDetail

    fun getAnimeDetailByName(animeName: String) {
        viewModelScope.launch {
            _animeDetail.value = repository.getAnimeByName(animeName)
        }
    }

    fun addAnimeToList(animeName: String, animeLogo: String, listName: String, onComplete: (Boolean, String?) -> Unit) {
        watchRepository.addAnimeToList(animeName, animeLogo, listName, onComplete)
    }

    private val _watchLists = MutableLiveData<List<String>>()
    val watchLists: LiveData<List<String>>
        get() = _watchLists

    init {
        loadWatchLists()
    }

    private fun loadWatchLists() {
        watchRepository.getWatchLists { success, watchLists, error ->
            if (success) {
                _watchLists.postValue(watchLists ?: emptyList())
            } else {
                Log.e("AnimeDetailViewModel", "An error occurred while loading watchlist.: $error")
            }
        }
    }
}