package com.sol.recipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.recipeapp.data.Recipe

class RecipeViewModel : ViewModel() {
    private val _recipeState = MutableLiveData<RecipeState>(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    init {
        Log.i("!!!", "RecipeViewModel")
        _recipeState.value = _recipeState.value?.copy(isFavorite = true)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}