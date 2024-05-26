package com.fleaudie.amvflix.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.adapters.WatchListAdapter
import com.fleaudie.amvflix.databinding.FragmentMyListBinding
import com.fleaudie.amvflix.databinding.PopupAddListBinding
import com.fleaudie.amvflix.viewmodel.MyListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyListFragment : Fragment() {
    private lateinit var binding: FragmentMyListBinding
    private val viewModel: MyListViewModel by viewModels()
    private lateinit var adapter: WatchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.btnAddList.setOnClickListener {
            showAddListPopup()
        }
        getWatchLists()
    }

    private fun setupRecyclerView() {
        adapter = WatchListAdapter(emptyList()) { listName ->
            showRemoveListConfirmationDialog(listName)
        }
        binding.rcyMyList.adapter = adapter
        binding.rcyMyList.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.watchLists.observe(viewLifecycleOwner) { watchLists ->
            adapter.listNames = watchLists
            adapter.notifyDataSetChanged()
        }
    }

    private fun showAddListPopup() {
        val inflater = LayoutInflater.from(context)
        val popupBinding = PopupAddListBinding.inflate(inflater, null, false)
        val view = popupBinding.root

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val editTextListName = view.findViewById<EditText>(R.id.editTextListName)
        val buttonSave = view.findViewById<Button>(R.id.buttonSaveList)

        buttonSave.setOnClickListener {
            val listName = editTextListName.text.toString().trim()
            if (listName.isNotEmpty()) {
                viewModel.addWatchList(listName)
                popupWindow.dismiss()
            } else {
                Toast.makeText(requireContext(), getString(R.string.list_name_no_null), Toast.LENGTH_SHORT).show()
            }
        }

        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
    }

    private fun showRemoveListConfirmationDialog(listName: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.remove_list))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                removeList(listName)
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun removeList(listName: String) {
        viewModel.removeWatchList(listName)
    }

    private fun getWatchLists() {
        viewModel.getWatchLists()
    }

}