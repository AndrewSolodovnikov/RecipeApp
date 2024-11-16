package com.sol.recipeapp.ui.category

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.R
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.databinding.ItemCategoryBinding
import java.io.InputStream


class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    var itemClickListener: OnItemClickListener? = null

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

        try {
            val inputStream: InputStream? = viewHolder.itemView.context?.assets?.open(dataSet[position].imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            viewHolder.binding.itemCategoryImage.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("MyLogError", "Image ${dataSet[position].imageUrl} not found")
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateData(newCategories: List<Category>) {
        dataSet = newCategories
        notifyDataSetChanged()
    }

}