package com.fleaudie.amvflix.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.databinding.ItemMyListBinding

class WatchListAdapter(var listNames: List<String>, private val onDeleteClickListener: (String) -> Unit) : RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMyListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listName: String) {
            binding.txtListName.text = listName
            binding.root.setOnClickListener {
                val bundle = bundleOf("listName" to listName)
                it.findNavController().navigate(R.id.action_myListFragment_to_listDetailFragment, bundle)
            }
            binding.imgRemoveList.setOnClickListener {
                onDeleteClickListener.invoke(listName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNames[position])
    }

    override fun getItemCount(): Int {
        return listNames.size
    }

}