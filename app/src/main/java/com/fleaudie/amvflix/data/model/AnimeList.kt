package com.fleaudie.amvflix.data.model

import com.google.gson.annotations.SerializedName

data class AnimeList(
    @SerializedName("name")
    val animeName: String,
    @SerializedName("embed_id")
    val embedId: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("main_characters")
    val characters: List<String>,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("head_photo")
    val coverPhoto: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("logo")
    val animeLogo: String,
    @SerializedName("detail_photos")
    val detailPhotos: List<String>
)
