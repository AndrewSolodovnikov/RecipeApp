package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.data.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private val sharedPref by lazy {
        application.getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    fun loadRecipe(recipeArgs: Recipe) {
        val isFavorite: Boolean = getFavorites().contains(recipeArgs.id.toString())
        _recipeState.value = _recipeState.value?.copy(
            recipe = recipeArgs,
            isFavorite = isFavorite,
        )
    }

    fun updatePortionCount(progress: Int) {
        val updatedState = recipeState.value?.copy(
            portionCount = progress
        )

        _recipeState.value = updatedState
    }

    private fun getFavorites(): MutableSet<String> {
        return HashSet(
            sharedPref
                .getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet()) ?: mutableSetOf()
        )
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val currentState = recipeState.value

        val newState = currentState?.copy(
            isFavorite = !currentState.isFavorite,
            portionCount = currentState?.portionCount ?: 1
        )
        _recipeState.value = newState

        val recipeId = currentState?.recipe?.id.toString()
        if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
        } else {
            favorites.add(recipeId)
        }

        saveFavorites(favorites)
    }

    private fun saveFavorites(favoriteIds: Set<String>) {
        with(sharedPref.edit()) {
            putStringSet(ARG_FAVORITES_SHARED_PREF, favoriteIds)
            apply()
        }
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}