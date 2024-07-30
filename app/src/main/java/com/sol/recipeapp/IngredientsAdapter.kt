package com.sol.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var quantity = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]

        with(viewHolder.binding) {
            tvItemCookingIngredientDescription.text = ingredient.description
            tvItemCookingIngredientDescriptionUnitOfMeasure.text = ingredient.unitOfMeasure

            val newQuantity = ingredient.quantity.toFloat() * quantity
            val quantityText = if (newQuantity % 1 == 0f) {
                newQuantity.toInt().toString()
            } else {
                String.format("%.1f", newQuantity)
            }
            tvItemCookingIngredientQuantity.text = quantityText + " "
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}
