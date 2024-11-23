package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import android.content.Context
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
    private val repository = RecipesRepository(context = application)

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val category = repository.getCategoryByIdSync(categoryId)
            val recipesList = repository.getRecipesByCategoryIdSync(categoryId)

            if (category != null && recipesList != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        category = category,
                        recipeList = recipesList,
                    )
                )
            } else if (category != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        category = category,
                        isError = true,
                    )
                )
            } else if (recipesList != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        recipeList = recipesList,
                        isError = true,
                    )
                )
            }
        }
    }

    data class RecipeListState(
        val recipeList: List<Recipe>? = emptyList(),
        val category: Category? = null,
        val isError: Boolean = false,
    )
}