package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Ingredient
import com.sol.recipeapp.data.Recipe
import java.math.BigDecimal

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeStateMap = mutableMapOf<Int, RecipeState>()
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
        // Если рецепт уже загружен, просто используем его состояние
        if (recipeStateMap.containsKey(recipeId)) {
            _recipeState.value = recipeStateMap[recipeId]
            return
        }

        // Загружаем рецепт и сохраняем его в мапу состояний
        val recipe = STUB.getRecipeById(recipeId)
        val recipeState = RecipeState(
            recipe = recipe,
            isFavorite = getFavorites().contains(recipeId.toString())
        )

        // Сохраняем оригинальные ингредиенты
        originalIngredientsMap[recipeId] = recipe?.ingredients

        // Сохраняем состояние в мапу и обновляем LiveData
        recipeStateMap[recipeId] = recipeState
        _recipeState.value = recipeState
    }

    fun updatePortionCount(recipeId: Int, count: Int) {
        val currentState = recipeStateMap[recipeId]
        val originalIngredients = originalIngredientsMap[recipeId]

        if (currentState != null && currentState.recipe?.id == recipeId) {
            val updatedIngredients = originalIngredients?.map { ingredient ->
                val newQuantity = try {
                    BigDecimal(ingredient.quantity).multiply(BigDecimal(count))
                } catch (e: NumberFormatException) {
                    BigDecimal.ZERO
                }
                ingredient.copy(quantity = newQuantity.toString())
            }

            val updatedState = currentState.copy(
                portionCount = count,
                recipe = currentState.recipe.copy(ingredients = updatedIngredients ?: emptyList())
            )

            // Сохраняем обновлённое состояние обратно в мапу и в LiveData
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

        val currentPortionCount = currentState?.portionCount ?: 1

        val newState = currentState?.copy(
            isFavorite = !currentState.isFavorite,
            portionCount = currentPortionCount
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
        var portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}
