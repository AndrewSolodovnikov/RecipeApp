package com.sol.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.ARG_FAVORITES_SHARED_PREF
import com.sol.recipeapp.data.Recipe
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState
    private val repository = RecipesRepository()

    private val sharedPref =
        application.getSharedPreferences(ARG_FAVORITES_SHARED_PREF, Context.MODE_PRIVATE)

    private fun getFavorites(): MutableSet<String>? {
        return sharedPref.getStringSet(ARG_FAVORITES_SHARED_PREF, HashSet())?.toMutableSet()
    }

    fun loadFavoritesRecipes() {
        viewModelScope.launch {
            val favoriteIds = getFavorites()?.mapNotNull { it.toIntOrNull() }?.toSet()
            Log.i("!!!fav", "favoriteIds $favoriteIds")
            favoriteIds?.let {
                if (favoriteIds.isNotEmpty()) {
                    val favoriteRecipes = repository.getRecipesByIdsSync(it)
                    if (favoriteRecipes != null) {
                        _favoritesState.postValue(
                            _favoritesState.value?.copy(dataSet = favoriteRecipes)
                        )
                    } else {
                        _favoritesState.postValue(
                            _favoritesState.value?.copy(dataSet = null)
                        )
                    }
                } else {
                    _favoritesState.postValue(
                        _favoritesState.value?.copy(dataSet = emptyList())
                    )
                }
            }
        }
    }

    data class FavoritesState(
        val dataSet: List<Recipe>? = emptyList(),
    )

}