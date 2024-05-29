package com.fleaudie.amvflix.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.amvflix.R
import com.fleaudie.amvflix.data.model.FavoriteCategory
import com.fleaudie.amvflix.data.model.GenreItem
import com.fleaudie.amvflix.databinding.ItemCategoriesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        checkIfFavorite(genre.genreName) { isFavorite ->
            if (isFavorite) {
                binding.btnFavCategory.setImageResource(R.drawable.favorite)
            } else {
                binding.btnFavCategory.setImageResource(R.drawable.favorite_border)
            }
        }

        binding.btnFavCategory.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val db = FirebaseFirestore.getInstance()
                val favoriteCategoryRef = db.collection("users").document(currentUser.uid)
                    .collection("favCategories").document(genre.genreName)

                favoriteCategoryRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        favoriteCategoryRef.delete().addOnSuccessListener {
                            binding.btnFavCategory.setImageResource(R.drawable.favorite_border)
                        }
                    } else {
                        val favoriteCategory = FavoriteCategory(genre.genreName, genre.imageUrl)
                        favoriteCategoryRef.set(favoriteCategory).addOnSuccessListener {
                            binding.btnFavCategory.setImageResource(R.drawable.favorite)
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(mContext, "Favouring operation failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(mContext, "Please log in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = genreList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setGenreList(genreList: List<GenreItem>) {
        this.genreList.clear()
        this.genreList.addAll(genreList)
        notifyDataSetChanged()
    }

    private fun checkIfFavorite(genreName: String, callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val favoriteCategoryRef = db.collection("users").document(currentUser.uid)
                .collection("favCategories").document(genreName)

            favoriteCategoryRef.get().addOnSuccessListener { document ->
                callback(document.exists())
            }.addOnFailureListener {
                Toast.makeText(mContext, "Error checking favorite category", Toast.LENGTH_SHORT).show()
                callback(false)
            }
        } else {
            callback(false)
        }
    }
}
