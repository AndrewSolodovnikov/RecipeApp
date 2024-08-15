package com.sol.recipeapp.ui.recipes.recipe

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.sol.recipeapp.data.Ingredient
import com.sol.recipeapp.data.Recipe
import kotlinx.parcelize.Parcelize

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )

    @Parcelize
    data class Recipe(
        val id: Int,
        val title: String,
        val ingredients: List<Ingredient>,
        val method: List<String>,
        val imageUrl: String,
    ): Parcelable
}