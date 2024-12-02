package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.data.FavoritesRepository
import com.sol.recipeapp.data.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState
    private val repository = FavoritesRepository(context = application)

    fun loadRecipe(recipeArgs: Recipe) {
        viewModelScope.launch {
            val isFavorite = repository.getFavoriteFromCache(recipeArgs.id) == 1
            _recipeState.value = _recipeState.value?.copy(
                recipe = recipeArgs,
                isFavorite = isFavorite
            )
        }
    }

    fun updatePortionCount(progress: Int) {
        val updatedState = recipeState.value?.copy(
            portionCount = progress
        )
        _recipeState.value = updatedState
    }

    fun onFavoriteClicked() {
        val currentState = recipeState.value
        currentState?.recipe?.let { recipe ->
            val isFavorite = !(currentState.isFavorite)

            viewModelScope.launch {
                repository.updateFavoriteFromCache(recipe.id, if (isFavorite) 1 else 0)

                _recipeState.value = currentState.copy(
                    isFavorite = isFavorite
                )
            }
        }
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}