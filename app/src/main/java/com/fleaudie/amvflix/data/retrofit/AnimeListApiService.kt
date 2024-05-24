package com.fleaudie.amvflix.data.retrofit

import com.fleaudie.amvflix.data.model.AnimeListResponse
import retrofit2.http.GET

interface AnimeListApiService {
    @GET("fleaudie/api/main/amvflix_animelist")
    suspend fun getAnimeData(): AnimeListResponse
}