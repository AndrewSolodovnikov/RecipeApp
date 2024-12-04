package com.sol.recipeapp.di

import com.sol.recipeapp.data.FavoritesRepository
import com.sol.recipeapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val favoritesRepository: FavoritesRepository,
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(favoritesRepository)
    }
}