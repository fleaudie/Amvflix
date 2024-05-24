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
class CategoryListViewModel @Inject constructor(private var repository: AnimeListRepository) : ViewModel() {

    private val _animeListByCategory = MutableLiveData<List<AnimeList>>()
    val animeListByCategory: LiveData<List<AnimeList>> get() = _animeListByCategory

    fun getAnimeListByCategory(categoryName: String) {
        viewModelScope.launch {
            val animeList = repository.getAnimeListByCategory(categoryName)
            _animeListByCategory.value = animeList
        }
    }
}