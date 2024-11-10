package com.sol.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.STUB
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.serialization.Serializable
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.ExecutorService

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState
    private val executorService: ExecutorService by lazy { (application as MyApplication).executorService }
    private val service = RecipesRepository()

    private val sharedPref by lazy {
        application.getSharedPreferences(
            ARG_FAVORITES_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    fun loadRecipe(recipeId: String) {
        executorService.submit {
            try {
                _recipeState.postValue(
                        _recipeState.value?.copy(
                            recipe = service.recipesByIdSync(recipeId),
                            isFavorite = getFavorites().contains(recipeId),
                            //recipeImage = drawable
                        )
                    )

            } catch (e: Exception) {
                e.printStackTrace()
                _recipeState.postValue(
                    _recipeState.value?.copy(
                        recipe = null
                    )
                )
            }
        }

    }

    fun updatePortionCount(progress: Int) {
        Log.i("!!!info", "seekBar_1 ViewModel seekBar progress = $progress")
        Log.i("!!!info", "Current portionCount before update = ${recipeState.value?.portionCount}")

        val updatedState = recipeState.value?.copy(
            portionCount = progress
        )

        Log.i("!!!info", "Updating portionCount to = $progress")
        _recipeState.value = updatedState
        Log.i("!!!info", "seekBar_4 ViewModel recipeState = ${recipeState.value}")
    }

    private fun getFavorites(): MutableSet<String> {
        return HashSet(
            sharedPref
                .getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet()) ?: mutableSetOf()
        )
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val currentState = recipeState.value

        val newState = currentState?.copy(
            isFavorite = !currentState.isFavorite,
            portionCount = currentState?.portionCount ?: 1
        )
        _recipeState.value = newState

        val recipeId = currentState?.recipe?.firstOrNull()?.id.toString()
        if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
        } else {
            favorites.add(recipeId)
        }

        saveFavorites(favorites)
    }

    private fun saveFavorites(favoriteIds: Set<String>) {
        with(sharedPref.edit()) {
            putStringSet(ARG_FAVORITES_SHARED_PREF, favoriteIds)
            apply()
        }
    }

    data class RecipeState(
        val recipe: List<Recipe>? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
        val recipeImage: Drawable? = null,
    )
}