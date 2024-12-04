package com.sol.recipeapp.di

import com.sol.recipeapp.data.FavoritesRepository
import com.sol.recipeapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val favoritesRepository: FavoritesRepository,
): Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(favoritesRepository)
    }
}