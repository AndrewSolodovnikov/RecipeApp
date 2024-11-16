package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import java.util.concurrent.ExecutorService

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState
    private val executorService: ExecutorService by lazy { (application as MyApplication).executorService }
    private val service = RecipesRepository()

    private val sharedPref by lazy {
        application.getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    fun loadRecipe(recipeId: Int) {
        executorService.submit {
            val recipe = service.getRecipeByIdSync(recipeId)
            if (recipe != null) {
                _recipeState.postValue(
                        _recipeState.value?.copy(
                            recipe = recipe,
                            isFavorite = getFavorites().contains(recipeId.toString()),
                            recipeImageUrl = recipe.imageUrl,
                            isError = false,
                        )
                    )

            } else {
                _recipeState.postValue(
                    _recipeState.value?.copy(
                        recipe = null,
                        isError = true,
                    )
                )
            }
        }

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
        val recipeImageUrl: String = "",
        val isError: Boolean = false,
    )
}