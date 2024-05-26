package com.fleaudie.amvflix.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.amvflix.databinding.ItemListSelectionBinding

class ListSelectionAdapter(
    private val listNames: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ListSelectionAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemListSelectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listName: String) {
            binding.txtItemListName.text = listName
            binding.root.setOnClickListener { onItemClick(listName) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNames[position])
    }

    override fun getItemCount(): Int {
        return listNames.size
    }
}