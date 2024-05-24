package com.fleaudie.amvflix.repositories

import com.fleaudie.amvflix.data.datasource.AnimeListDataSource
import com.fleaudie.amvflix.data.model.AnimeList
import com.fleaudie.amvflix.data.model.AnimeListResponse
import com.fleaudie.amvflix.data.model.GenreItem

class AnimeListRepository(private val dataSource: AnimeListDataSource) {
    suspend fun getAnimeData(): AnimeListResponse {
        return dataSource.getAnimeData()
    }

    suspend fun getAnimeListByCategory(categoryName: String): List<AnimeList> = dataSource.getAnimeListByCategory(categoryName)

    suspend fun getAnimeByName(animeName: String): AnimeList? {
        return dataSource.getAnimeByName(animeName)
    }

}