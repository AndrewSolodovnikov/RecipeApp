package com.sol.recipeapp.ui.recipes.recipeslist

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
            try {
                val recipesList = repository.recipesByCategoryIdSync(categoryId) ?: emptyList()
                val category = repository.categoryByIdSync(categoryId)
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

                Log.i("!!!info", "img $drawable")

                _recipeListState.postValue(
                    _recipeListState.value?.copy(
                        dataSet = recipesList,
                        categoryTitle = category?.title ?: "",
                        categoryImageUrl = drawable
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("!!!e", "load recipe", e)
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
        val categoryImageUrl: Drawable? = null,
        val categoryTitle: String = "",
    )
}