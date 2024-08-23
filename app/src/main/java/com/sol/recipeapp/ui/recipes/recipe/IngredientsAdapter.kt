package com.sol.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.data.Ingredient
import com.sol.recipeapp.databinding.ItemIngredientBinding
import java.math.BigDecimal

class IngredientsAdapter(private val dataSet: List<Ingredient> = emptyList()) :
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

            val newQuantity = BigDecimal(ingredient.quantity).multiply(BigDecimal(quantity))
            val quantityText = if (newQuantity.remainder(BigDecimal.ONE) == BigDecimal.ZERO) {
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
