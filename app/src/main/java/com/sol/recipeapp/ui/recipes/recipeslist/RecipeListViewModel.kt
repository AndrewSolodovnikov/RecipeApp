package com.sol.recipeapp.ui.recipes.recipeslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.launch

class RecipeListViewModel(
    private val repository: RecipesRepository
) : ViewModel() {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val recipesList = repository.getRecipesFromCacheByCategoryId(categoryId)

            if (recipesList.isNotEmpty()) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        recipeList = recipesList,
                    )
                )
            } else {
                val recipesList = repository.getRecipesByCategoryIdSync(categoryId)
                repository.insertRecipesFromCache(recipesList?.map { it.copy(categoryId = categoryId) })
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        recipeList = recipesList,
                        isError = recipesList == null
                    )
                )
            }
        }
    }

    fun loadCategory(categoryArgs: Category) {
        _recipeListState.value = _recipeListState.value?.copy(
            category = categoryArgs
        )
    }
}

data class RecipeListState(
    val recipeList: List<Recipe>? = emptyList(),
    val category: Category? = null,
    val isError: Boolean = false,
)