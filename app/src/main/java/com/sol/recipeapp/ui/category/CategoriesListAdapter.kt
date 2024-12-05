package com.sol.recipeapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.R
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.databinding.ItemCategoryBinding


class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder){
            with(dataSet[position]){
                binding.itemCategoryTitleText.text = this.title
                binding.itemCategoryDescriptionText.text = this.description
                binding.itemCategoryImage.contentDescription = viewHolder.itemView.context
                    .getString(R.string.list_item_category_image) + " " + this.title
                binding.itemCategory.setOnClickListener { itemClickListener?.onItemClick(this.id) }
            }
        }

        val imageView: ImageView = viewHolder.binding.itemCategoryImage
        val imageUrl = "$BASE_URL$IMAGE_CATEGORY_URL${dataSet[position].imageUrl}"
        Glide.with(viewHolder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateData(newCategories: List<Category>) {
        dataSet = newCategories
        notifyDataSetChanged()
    }

}