package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import android.util.Log
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
            val recipesList = repository.getRecipesFromCacheByCategoryId(categoryId)
            val category = repository.getCategoryByIdSync(categoryId)

            Log.i("!!!db", "db recipe $recipesList")

            if (recipesList.isNotEmpty()) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        category = category,
                        recipeList = recipesList,
                        isError = category == null
                    )
                )
            } else {
                val recipesList = repository.getRecipesByCategoryIdSync(categoryId)
                Log.i("!!!db", "Running Retrofit")
                Log.i("!!!db", "Retrofit recipeList $recipesList")
                repository.insertRecipesFromCache(recipesList?.map { it.copy(categoryId = categoryId) })
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        category = category,
                        recipeList = recipesList,
                        isError = category == null || recipesList == null
                    )
                )
            }
        }
    }
}

data class RecipeListState(
    val recipeList: List<Recipe>? = emptyList(),
    val category: Category? = null,
    val isError: Boolean = false,
)