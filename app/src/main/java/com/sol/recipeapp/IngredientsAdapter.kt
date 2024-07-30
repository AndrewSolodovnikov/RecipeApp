package com.sol.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            with(dataSet[position]) {
                binding.itemCookingIngredientDescription.text = this.description
                binding.itemCookingIngredientQuantity.text = this.quantity + " "
                binding.itemCookingIngredientDescriptionUnitOfMeasure.text = this.unitOfMeasure
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}