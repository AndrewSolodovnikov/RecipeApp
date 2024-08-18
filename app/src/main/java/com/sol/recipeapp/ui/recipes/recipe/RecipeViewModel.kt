package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData<RecipeState>(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private val sharedPref by lazy {
        application.getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    init {
        Log.i("!!!", "RecipeViewModel")
        _recipeState.value = _recipeState.value?.copy(isFavorite = true)
    }

    fun loadRecipe(recipeId: Int) {
        // TODO load from network
        val recipe = STUB.getRecipeById(recipeId)

        _recipeState.value = _recipeState.value?.let {
            RecipeState(
                recipe = recipe,
                isFavorite = getFavorites().contains(recipeId.toString()),
                portionCount = it.portionCount
            )
        }
    }

    private fun getFavorites(): MutableSet<String> {
        return HashSet(sharedPref
            .getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet()) ?: mutableSetOf())
    }

    /*
private fun toggleFavorite() {
    val favorites = getFavorites()
    val recipeId = recipe?.id.toString()
    if (favorites.contains(recipeId)) {
        favorites.remove(recipeId)
        binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
    } else {
        favorites.add(recipeId)
        binding.btnFavorite.setImageResource(R.drawable.ic_heart)
    }
    saveFavorites(favorites)
}

private fun updateFavoriteIcon() {
    val favorites = getFavorites()
    val recipeId = recipe?.id.toString()
    if (favorites.contains(recipeId)) {
        binding.btnFavorite.setImageResource(R.drawable.ic_heart)
    } else {
        binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
    }
}

 */
    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val isFavorite = _recipeState.value?.isFavorite ?: false
        val recipeId = _recipeState.value?.recipe?.id.toString()
        if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            //binding.btnFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            favorites.add(recipeId)
            //binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
        }
        _recipeState.value = _recipeState.value?.copy(isFavorite = !isFavorite)

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
        var portionCount: Int = 1,
        var isFavorite: Boolean = false,
    )
}