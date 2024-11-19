package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.launch

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState
    private val repository = RecipesRepository()

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val category = repository.getCategoryByIdSync(categoryId)
            val recipesList = repository.getRecipesByCategoryIdSync(categoryId)

            _recipeListState.value = _recipeListState.value?.copy(
                category = category,
                recipeList = recipesList
            )
        }
    }

    data class RecipeListState(
        val recipeList: List<Recipe>? = emptyList(),
        val category: Category? = null,
    )
}