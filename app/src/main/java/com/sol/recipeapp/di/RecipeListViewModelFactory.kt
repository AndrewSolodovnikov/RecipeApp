package com.sol.recipeapp.di

import com.sol.recipeapp.data.RecipesRepository
import com.sol.recipeapp.ui.recipes.recipeslist.RecipeListViewModel

class RecipeListViewModelFactory(
    private val recipesRepository: RecipesRepository,
): Factory<RecipeListViewModel> {

    override fun create(): RecipeListViewModel {
        return RecipeListViewModel(recipesRepository)
    }
}