package com.sol.recipeapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.data.FavoritesRepository
import com.sol.recipeapp.data.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState
    private val repository = FavoritesRepository(context = application)

    fun loadFavoritesRecipes() {
        viewModelScope.launch {
            val favoritesList: List<Recipe> = repository.getFavoritesListFromCache()
            if (favoritesList.isNotEmpty()) {
                _favoritesState.postValue(
                    _favoritesState.value?.copy(dataSet = favoritesList)
                )
            } else {
                _favoritesState.postValue(
                    _favoritesState.value?.copy(dataSet = emptyList())
                )
            }
        }
    }

    data class FavoritesState(
        val dataSet: List<Recipe>? = emptyList(),
    )
}