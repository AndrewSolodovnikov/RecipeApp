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

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState

    private var drawable: Drawable? = null

    fun loadRecipes(categoryId: Int) {
        //loading recipes
        val recipes = STUB.getRecipesByCategoryId(categoryId)

        //loading category data by id
        val category = STUB.getCategories().find { it.id == categoryId }

        //loading image
        try {
            val inputStream: InputStream? = category?.imageUrl.let {
                category?.imageUrl?.let { image ->
                    application.assets?.open(image)
                }
            }
            drawable = Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            Log.e("MyLogError", "Image $drawable not found")
        }

        if (category != null) {
            _recipeListState.value = _recipeListState.value?.copy(
                dataSet = recipes,
                categoryImageUrl = drawable,
                categoryTitle = category.title
            )
        }
    }

    data class RecipeListState(
        val dataSet: List<Recipe> = emptyList(),
        val categoryImageUrl: Drawable? = null,
        val categoryTitle: String = "",
    )
}