package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.Recipe

class RecipeListViewModel(application: Application): AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData<RecipeListState?>()
    val recipeListState = _recipeListState

    data class RecipeListState(
        val category: List<Category> = STUB.getCategories(),
        val recipe: Recipe? = null,
    )

}