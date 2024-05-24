package com.fleaudie.amvflix.data.model

import com.google.gson.annotations.SerializedName

data class AnimeListResponse(
    @SerializedName("anime")
    val anime: List<AnimeList>,
    @SerializedName("genre")
    val genre: Map<String, String>
)
