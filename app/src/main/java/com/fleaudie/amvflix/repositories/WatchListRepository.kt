package com.fleaudie.amvflix.repositories

import com.fleaudie.amvflix.data.datasource.WatchListDataSource
import com.fleaudie.amvflix.data.model.ListContent

class WatchListRepository(private val dataSource: WatchListDataSource) {

    fun addWatchList(listName: String, onComplete: (Boolean, String?) -> Unit) {
        dataSource.addWatchList(listName, onComplete)
    }

    fun getWatchLists(onComplete: (Boolean, List<String>?, String?) -> Unit) {
        dataSource.getWatchLists(onComplete)
    }

    fun removeWatchList(listName: String, onComplete: (Boolean, String?) -> Unit) {
        dataSource.removeWatchList(listName, onComplete)
    }

    fun addAnimeToList(animeName: String, animeLogo: String, listName: String, onComplete: (Boolean, String?) -> Unit) {
        dataSource.addAnimeToList(animeName, animeLogo, listName, onComplete)
    }

    fun getAnimeList(listName: String, onComplete: (Boolean, List<ListContent>?, String?) -> Unit) {
        dataSource.getAnimeList(listName, onComplete)
    }

    fun removeAnimeFromList(listName: String, anime: ListContent, onComplete: (Boolean, String?) -> Unit) {
        dataSource.removeAnimeFromList(listName, anime, onComplete)
    }
}