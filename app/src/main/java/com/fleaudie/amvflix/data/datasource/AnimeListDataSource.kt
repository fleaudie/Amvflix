package com.fleaudie.amvflix.data.datasource

import com.fleaudie.amvflix.data.model.AnimeList
import com.fleaudie.amvflix.data.model.AnimeListResponse
import com.fleaudie.amvflix.data.model.GenreItem
import com.fleaudie.amvflix.data.retrofit.AnimeListApiService

class AnimeListDataSource(private var apiService : AnimeListApiService) {

    suspend fun getAnimeData(): AnimeListResponse {
        return apiService.getAnimeData()
    }

    suspend fun getAnimeListByCategory(categoryName: String): List<AnimeList> {
        val animeListResponse = apiService.getAnimeData()

        val filteredAnimeList = animeListResponse.anime.filter { anime ->
            anime.tags.contains(categoryName)
        }

        return filteredAnimeList
    }

    suspend fun getAnimeByName(animeName: String): AnimeList? {
        val animeListResponse = apiService.getAnimeData()

        return animeListResponse.anime.find { anime ->
            anime.animeName == animeName
        }
    }

}