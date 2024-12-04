package com.sol.recipeapp.di

import com.sol.recipeapp.data.RecipesRepository
import com.sol.recipeapp.ui.category.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}