package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import java.util.concurrent.ExecutorService

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState
    private val executorService: ExecutorService by lazy {
        (application as MyApplication).executorService
    }
    private val repository = RecipesRepository()

    fun loadRecipes(categoryId: Int) {
        executorService.submit {
            val recipesList = repository.getRecipesByCategoryIdSync(categoryId)

            if (recipesList != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        recipeList = recipesList,
                    )
                )
            } else {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        recipeList = null,
                    )
                )
            }
        }
    }

    data class RecipeListState(
        val recipeList: List<Recipe>? = emptyList(),
    )
}