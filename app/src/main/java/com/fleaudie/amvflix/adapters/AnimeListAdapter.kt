package com.fleaudie.amvflix.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.amvflix.data.model.AnimeList
import com.fleaudie.amvflix.databinding.ItemCategoryListBinding

class AnimeListAdapter(private val context: Context) : RecyclerView.Adapter<AnimeListAdapter.AnimeViewHolder>() {
    private var animeList: List<AnimeList> = emptyList()

    inner class AnimeViewHolder(val binding: ItemCategoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemCategoryListBinding.inflate(layoutInflater, parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.binding.apply {
            txtListAnimeName.text = anime.animeName
            val tagString = anime.tags.joinToString(", ")
            txtListAnimeTags.text = tagString

            Glide.with(context)
                .load(anime.coverPhoto)
                .into(imgListCover)
        }

        holder.binding.root.setOnClickListener {
            onAnimeItemClickListener?.onAnimeItemClick(anime)
        }
    }

    interface OnAnimeItemClickListener {
        fun onAnimeItemClick(anime: AnimeList)
    }

    private var onAnimeItemClickListener: OnAnimeItemClickListener? = null

    fun setOnAnimeItemClickListener(listener: OnAnimeItemClickListener) {
        onAnimeItemClickListener = listener
    }

    override fun getItemCount(): Int = animeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAnimeList(newAnimeList: List<AnimeList>) {
        animeList = newAnimeList
        notifyDataSetChanged()
    }
}