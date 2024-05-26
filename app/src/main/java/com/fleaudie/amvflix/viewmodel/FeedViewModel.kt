package com.fleaudie.amvflix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleaudie.amvflix.data.model.AnimeListResponse
import com.fleaudie.amvflix.repositories.AnimeListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private var repository: AnimeListRepository) : ViewModel() {
    private val _animeList = MutableLiveData<AnimeListResponse>()
    val animeList : LiveData<AnimeListResponse> get() = _animeList


    private var isDataFetched = false

    fun getAnimeData() {
        if (!isDataFetched) {
            viewModelScope.launch {
                _animeList.value = repository.getAnimeData()
                isDataFetched = true
            }
        }
    }

}