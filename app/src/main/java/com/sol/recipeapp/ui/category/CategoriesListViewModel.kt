package com.sol.recipeapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecipesRepository()

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> = _categoriesListState

    fun loadCategory() {
        viewModelScope.launch {
            val dataSet = repository.getCategorySync()
            if (dataSet != null) {
                _categoriesListState.postValue(
                    _categoriesListState.value?.copy(dataSet = repository.getCategorySync())
                )
            } else {
                _categoriesListState.postValue(
                    _categoriesListState.value?.copy(dataSet = null)
                )
            }
        }
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = categoriesListState.value?.dataSet?.find { it.id == categoryId }
        _categoriesListState.postValue(
            _categoriesListState.value?.copy(navigateToCategory = category)
        )
    }

    fun clearNavigation() {
        _categoriesListState.value = _categoriesListState.value?.copy(navigateToCategory = null)
    }
}

data class CategoriesListState(
    val dataSet: List<Category>? = emptyList(),
    val navigateToCategory: Category? = null,
)