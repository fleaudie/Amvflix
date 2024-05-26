package com.fleaudie.amvflix.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.adapters.ListDetailAdapter
import com.fleaudie.amvflix.data.model.ListContent
import com.fleaudie.amvflix.databinding.FragmentListDetailBinding
import com.fleaudie.amvflix.viewmodel.ListDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDetailFragment : Fragment() {
    private lateinit var binding: FragmentListDetailBinding
    private val viewModel: ListDetailViewModel by viewModels()
    private lateinit var listDetailAdapter: ListDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listName = ListDetailFragmentArgs.fromBundle(requireArguments()).listName
        listDetailAdapter = ListDetailAdapter(mutableListOf()) { anime ->
            showRemoveConfirmationDialog(listName, anime)
        }

        binding.rcyListContent.adapter = listDetailAdapter
        binding.rcyListContent.layoutManager = LinearLayoutManager(requireContext())

        binding.txtDetailListName.text = listName

        viewModel.getAnimeList(listName)

        viewModel.animeList.observe(viewLifecycleOwner) { animeList ->
            if (animeList.isEmpty()) {
                Log.d("list detail", "Received empty anime list")
            }
            listDetailAdapter.updateList(animeList)
        }
    }

    private fun showRemoveConfirmationDialog(listName: String, anime: ListContent) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.remove_anime))
            .setMessage("${anime.animeName} : ${getString(R.string.remove_anime_from_list)}")
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                viewModel.removeAnimeFromList(listName, anime)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}