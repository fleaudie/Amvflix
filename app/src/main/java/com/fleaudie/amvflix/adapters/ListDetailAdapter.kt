package com.fleaudie.amvflix.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.amvflix.data.model.ListContent
import com.fleaudie.amvflix.databinding.ItemListContentBinding

class ListDetailAdapter(
    private var animeList: MutableList<ListContent>,
    private val onRemoveClick: (ListContent) -> Unit
) : RecyclerView.Adapter<ListDetailAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ListContent>) {
        animeList = newList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListContentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime)
    }

    override fun getItemCount(): Int {
        return animeList.size
    }

    inner class ViewHolder(private val binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: ListContent) {
            binding.apply {
                txtListItemAnimeName.text = anime.animeName
                Glide.with(root.context)
                    .load(anime.animeLogo)
                    .into(imageView6)
                txtRemoveFromList.setOnClickListener {
                    onRemoveClick(anime)
                }
            }
        }
    }

}