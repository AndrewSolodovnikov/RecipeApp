package com.sol.recipeapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val repository: RecipesRepository
) : ViewModel() {

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> = _categoriesListState

    fun loadCategory() {
        viewModelScope.launch {
            val categories: List<Category> = repository.getCategoriesFromCache()
            if (categories.isNotEmpty()) {
                _categoriesListState.postValue(
                    _categoriesListState.value?.copy(
                        dataSet = categories
                    )
                )
            } else {
                val dataSet = repository.getCategorySync()
                repository.insertCategoriesFromCache(dataSet)
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