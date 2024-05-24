package com.fleaudie.amvflix.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.data.model.GenreItem
import com.fleaudie.amvflix.databinding.FragmentFeedBinding
import com.fleaudie.amvflix.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.animeList.observe(viewLifecycleOwner) { animeListResponse ->
            animeListResponse?.let { response ->
                val animeNames = response.anime.map { it.animeName }
                Log.d("AnimeList", "Anime Names: $animeNames")

                val genreNames = response.genre.keys.toList()
                Log.d("AnimeList", "Genre Names: $genreNames")

                val genreItemList = response.genre.map { GenreItem(it.key, it.value) }
                Log.d("AnimeList", "Genre Items: $genreItemList")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAnimeData()
    }

}