package com.sol.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState

    private val sharedPref =
        application.getSharedPreferences(ARG_FAVORITES_SHARED_PREF, Context.MODE_PRIVATE)

    private fun getFavorites(): MutableSet<String>? {
        return sharedPref.getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet())?.toMutableSet()
    }

    fun loadFavoritesRecipes() {
        val favoriteIds = getFavorites()?.mapNotNull { it.toIntOrNull() }?.toSet()
        val favoriteRecipes = favoriteIds?.let { STUB.getRecipesByIds(it) }
        _favoritesState.value = _favoritesState.value?.copy(dataSet = favoriteRecipes)
    }

    data class FavoritesState(
        val dataSet: List<Recipe>? = emptyList(),
    )

}