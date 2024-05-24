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
import com.bumptech.glide.Glide
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.adapters.AnimeListAdapter
import com.fleaudie.amvflix.data.model.AnimeList
import com.fleaudie.amvflix.databinding.FragmentCategoryListBinding
import com.fleaudie.amvflix.viewmodel.CategoryListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryListBinding
    private val viewModel: CategoryListViewModel by viewModels()
    private lateinit var adapter: AnimeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryName = CategoryListFragmentArgs.fromBundle(requireArguments()).genreName
        val categoryImage = CategoryListFragmentArgs.fromBundle(requireArguments()).imageUrl

        binding.txtTagName.text = categoryName
        context?.let {
            Glide.with(it)
                .load(categoryImage)
                .into(binding.imgTagPhoto)
        }

        viewModel.getAnimeListByCategory(categoryName)

        adapter = AnimeListAdapter(requireContext())
        binding.rcyCategoryList.adapter = adapter
        binding.rcyCategoryList.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnAnimeItemClickListener(object : AnimeListAdapter.OnAnimeItemClickListener {
            override fun onAnimeItemClick(anime: AnimeList) {
                val action = CategoryListFragmentDirections.actionCategoryListFragmentToAnimeDetailFragment(anime.animeName)
                findNavController().navigate(action)
            }
        })

        viewModel.animeListByCategory.observe(viewLifecycleOwner) { animeList ->
            adapter.setAnimeList(animeList)
        }
    }

}