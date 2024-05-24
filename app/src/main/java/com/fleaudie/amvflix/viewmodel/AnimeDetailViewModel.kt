package com.fleaudie.amvflix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleaudie.amvflix.data.model.AnimeList
import com.fleaudie.amvflix.repositories.AnimeListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(private var repository: AnimeListRepository) : ViewModel(){

    private val _animeDetail = MutableLiveData<AnimeList>()
    val animeDetail: LiveData<AnimeList>
        get() = _animeDetail

    fun getAnimeDetailByName(animeName: String) {
        viewModelScope.launch {
            _animeDetail.value = repository.getAnimeByName(animeName)
        }
    }
}