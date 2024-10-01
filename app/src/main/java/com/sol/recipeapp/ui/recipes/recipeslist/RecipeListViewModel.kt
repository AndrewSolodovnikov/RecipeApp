package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Recipe
import java.io.InputStream

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState

    fun loadRecipes(categoryId: Int) {
        val recipes = STUB.getRecipesByCategoryId(categoryId)
        val newRecipeListState = _recipeListState.value?.copy(dataSet = recipes)
        _recipeListState.value = newRecipeListState
    }

    fun categoryImage(categoryImageUrl: String) {
        try {
            val inputStream: InputStream? = categoryImageUrl.let {
                getApplication<Application>().assets?.open(it)
            }
            val drawable = Drawable.createFromStream(inputStream, null)
            val newRecipeListState = _recipeListState.value?.copy(categoryImageUrl = drawable)
            _recipeListState.value = newRecipeListState
        } catch (e: Exception) {
            Log.e("MyLogError", "Image $categoryImageUrl not found")
        }
    }

    data class RecipeListState(
        val dataSet: List<Recipe> = emptyList(),
        val categoryImageUrl: Drawable? = null,
    )
}