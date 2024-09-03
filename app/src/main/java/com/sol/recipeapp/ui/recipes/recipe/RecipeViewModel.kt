package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Ingredient
import com.sol.recipeapp.data.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData<RecipeState?>()
    val recipeState: MutableLiveData<RecipeState?> = _recipeState
    private val originalIngredientsMap = mutableMapOf<Int, List<Ingredient>?>()

    private val sharedPref by lazy {
        application.getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    fun loadRecipe(recipeId: Int) {
        Log.i("!!!info", "____ start loadRecipe()")
        if (recipeState.value?.recipe?.id == recipeId) return

        val recipe = STUB.getRecipeById(recipeId)
        Log.i("!!!info", "init_1 ViewModel loadRecipe() recipe = $recipe")
        val newRecipeState = RecipeState(
            recipe = recipe,
            isFavorite = getFavorites().contains(recipeId.toString())
        )

        //originalIngredientsMap[recipeId] = recipe?.ingredients

        recipeState.value = newRecipeState
        Log.i("!!!info", "init_2, seekBar_3 ViewModel loadRecipe() recipeState = ${recipeState.value}")
    }

    fun updatePortionCount(recipeId: Int, progress: Int) {
        Log.i("!!!info", "seekBar_1 ViewModel seekBar progress = $progress")
        Log.i("!!!info", "seekBar_2 ViewModel seekBar recipeId = $recipeId")
        val currentState = recipeState.value


        if (currentState != null && currentState.recipe?.id == recipeId) {
            val updatedState = currentState.copy(
                portionCount = progress
            )

            recipeState.value = updatedState
            Log.i("!!!info", "seekBar_4 ViewModel recipeState = ${recipeState.value}")
        }
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
        recipeState.value = newState

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
