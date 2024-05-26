package com.fleaudie.amvflix.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.adapters.CategoriesAdapter
import com.fleaudie.amvflix.data.model.GenreItem
import com.fleaudie.amvflix.databinding.FragmentCategoriesBinding
import com.fleaudie.amvflix.viewmodel.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var adapter : CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoriesAdapter(requireContext(), mutableListOf())
        binding.categoryRcy.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRcy.layoutManager = layoutManager

        adapter.setOnItemClickListener { category ->
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToCategoryListFragment(category.genreName, category.imageUrl)
            findNavController().navigate(action)
        }

        viewModel.animeList.observe(viewLifecycleOwner) { animeList ->
            animeList?.let {
                val genreItemList = animeList.genre.map { GenreItem(it.key, it.value) }
                adapter.setGenreList(genreItemList)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAnimeData()
    }

}