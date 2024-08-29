package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Recipe
import java.math.BigDecimal

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeStateMap = mutableMapOf<Int, RecipeState>()
    private val _recipeState = MutableLiveData<RecipeState?>()
    val recipeState: MutableLiveData<RecipeState?> = _recipeState

    private val sharedPref by lazy {
        application.getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    fun loadRecipe(recipeId: Int) {
        if (recipeStateMap.containsKey(recipeId)) {
            _recipeState.value = recipeStateMap[recipeId]
            return
        }

        val recipe = STUB.getRecipeById(recipeId)
        val recipeState = RecipeState(
            recipe = recipe,
            isFavorite = getFavorites().contains(recipeId.toString())
        )

        recipeStateMap[recipeId] = recipeState
        _recipeState.value = recipeState
    }

    fun updatePortionCount(recipeId: Int, count: Int) {
        val currentState = recipeStateMap[recipeId]

        if (currentState != null && currentState.recipe?.id == recipeId) {
            val originalIngredients = currentState.recipe.ingredients
            val updatedIngredients = originalIngredients.map { ingredient ->
                val newQuantity = try {
                    BigDecimal(ingredient.quantity).multiply(BigDecimal(count))
                } catch (e: NumberFormatException) {
                    BigDecimal.ZERO
                }
                ingredient.copy(quantity = newQuantity.toString())
            }

            val updatedState = currentState.copy(
                portionCount = count,
                recipe = currentState.recipe.copy(ingredients = updatedIngredients)
            )

            recipeStateMap[recipeId] = updatedState
            _recipeState.value = updatedState
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
        val currentState = _recipeState.value

        val newState = currentState?.copy(isFavorite = !currentState.isFavorite)
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
        var portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}
