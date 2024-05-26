package com.fleaudie.amvflix.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.amvflix.data.model.GenreItem
import com.fleaudie.amvflix.databinding.ItemCategoriesBinding

class CategoriesAdapter(private var mContext: Context, private var genreList: MutableList<GenreItem>) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var design: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(design.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val design = ItemCategoriesBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(design)
    }

    private var onItemClickListener: ((GenreItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (GenreItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val genre = genreList[position]
        val binding = holder.design
        binding.txtCategory.text = genre.genreName
        Glide.with(mContext)
            .load(genre.imageUrl)
            .into(binding.imageCategory)

        binding.root.setOnClickListener {
            onItemClickListener?.invoke(genre)
        }
    }

    override fun getItemCount(): Int = genreList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGenreList(genreList: List<GenreItem>) {
        this.genreList.clear()
        this.genreList.addAll(genreList)
        notifyDataSetChanged()
    }
}
