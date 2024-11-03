package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.STUB
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import java.io.InputStream
import java.util.concurrent.ExecutorService

class RecipeListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _recipeListState = MutableLiveData(RecipeListState())
    val recipeListState: LiveData<RecipeListState> = _recipeListState
    private val executorService: ExecutorService by lazy {
        (application as MyApplication).executorService
    }
    private val repository = RecipesRepository()

    private var drawable: Drawable? = null

    fun loadRecipes(categoryId: Int) {
        executorService.submit {
            Log.i("!!!info", "Repository ${repository.recipesByCategoryIdSync(categoryId)}")
            Log.i("!!!info", "CategoryId $categoryId")
            try {
                _recipeListState.postValue(
                    repository.recipesByCategoryIdSync(categoryId)?.let {
                        _recipeListState.value?.copy(
                            dataSet = it
                        )
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        /*
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

         */
    }

    data class RecipeListState(
        val dataSet: List<Recipe> = emptyList(),
        val categoryImageUrl: Drawable? = null,
        val categoryTitle: String = "",
    )
}