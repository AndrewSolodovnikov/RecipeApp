package com.sol.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sol.recipeapp.data.Ingredient
import com.sol.recipeapp.databinding.ItemIngredientBinding
import java.math.BigDecimal

class IngredientsAdapter(
    private var dataSet: List<Ingredient> = emptyList(),
    private var portionCount: Int = 1,
) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]

        val newQuantity = try {
            BigDecimal(ingredient.quantity).multiply(BigDecimal(portionCount))
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }

        with(viewHolder.binding) {
            tvItemCookingIngredientDescription.text = ingredient.description
            tvItemCookingIngredientDescriptionUnitOfMeasure.text = ingredient.unitOfMeasure
            tvItemCookingIngredientQuantity.text = newQuantity.toString()
            dividerIngredient.visibility =
                if (position == dataSet.size - 1) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun updateIngredientsList(data: List<Ingredient>) {
        dataSet = data
        notifyDataSetChanged()
    }

    fun updatePortionCount(newQuantity: Int) {
        portionCount = newQuantity
        notifyDataSetChanged()
    }
}
