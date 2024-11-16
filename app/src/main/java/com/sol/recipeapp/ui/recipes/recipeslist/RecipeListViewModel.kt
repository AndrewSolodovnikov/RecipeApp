package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.IMAGE_CATEGORY_URL
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
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
            val category = repository.getCategoryByIdSync(categoryId)

            if (recipesList != null) {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        dataSet = recipesList,
                        categoryTitle = category?.title ?: "",
                        categoryImageUrl = BASE_URL + IMAGE_CATEGORY_URL + category?.imageUrl
                    )
                )
            } else {
                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        dataSet = null
                    )
                )
            }
        }
    }

    data class RecipeListState(
        val dataSet: List<Recipe>? = emptyList(),
        val categoryImageUrl: String = "",
        val categoryTitle: String = "",
    )
}