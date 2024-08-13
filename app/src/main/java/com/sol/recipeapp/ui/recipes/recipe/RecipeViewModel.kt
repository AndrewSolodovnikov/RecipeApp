package com.sol.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.sol.recipeapp.data.Ingredient

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val id: Int? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = emptyList(),
        val method: List<String> = emptyList(),
        val imageUrl: String? = null,
    )
}