package com.sol.recipeapp.ui.recipes.recipeslist

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.databinding.ItemRecipeBinding
import java.io.InputStream


class RecipesListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder){
            with(dataSet[position]){
                binding.itemRecipeTitleText.text = this.title
                binding.itemRecipeImage.contentDescription = viewHolder.itemView.context
                    .getString(R.string.list_item_recipe_image) + " " + this.title
                binding.itemCategoryRecipe.setOnClickListener { itemClickListener?.onItemClick(this) }
            }
        }

        val imageView: ImageView = viewHolder.binding.itemRecipeImage
        val imageUrl = "$BASE_URL$IMAGE_CATEGORY_URL${dataSet[position].imageUrl}"
        Glide.with(viewHolder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageView)

        try {
            val inputStream: InputStream? = viewHolder.itemView.context?.assets?.open(dataSet[position].imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            viewHolder.binding.itemRecipeImage.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("MyLogError", "Image ${dataSet[position].imageUrl} not found")
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        dataSet = newRecipes
        notifyDataSetChanged()
    }

}