package com.fleaudie.amvflix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fleaudie.amvflix.data.model.ListContent
import com.fleaudie.amvflix.repositories.WatchListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(private var repository: WatchListRepository) : ViewModel() {
    private val _animeList = MutableLiveData<List<ListContent>>()
    val animeList: LiveData<List<ListContent>> = _animeList

    fun getAnimeList(listName: String) {
        repository.getAnimeList(listName) { success, animeList, _ ->
            if (success) {
                _animeList.value = animeList ?: emptyList()
            }
        }
    }

    fun removeAnimeFromList(listName: String, anime: ListContent) {
        repository.removeAnimeFromList(listName, anime) { success, _ ->
            if (success) {
                getAnimeList(listName)
            }
        }
    }
}