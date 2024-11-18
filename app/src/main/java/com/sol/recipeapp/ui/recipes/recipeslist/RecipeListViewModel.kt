package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState
    private val executorService: ExecutorService by lazy {
        (application as MyApplication).executorService
    }
    private val repository = RecipesRepository()

    suspend fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val category = repository.getCategoryByIdSync(categoryId)
            val recipesList = repository.getRecipesByCategoryIdSync(categoryId)

            if (recipesList != null && category != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        category = category,
                        recipeList = recipesList,
                    )
                )
            } else if (recipesList != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        recipeList = recipesList,
                        isError = true,
                    )
                )
            } else if (category != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        category = category,
                        isError = true,
                    )
                )
            } else {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
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