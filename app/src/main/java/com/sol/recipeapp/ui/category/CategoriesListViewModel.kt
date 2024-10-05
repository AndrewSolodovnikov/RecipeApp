package com.sol.recipeapp.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Category

class CategoriesListViewModel: ViewModel() {
    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState = _categoriesListState

    init {
        val newDataSet = STUB.getCategories()
        _categoriesListState.value = _categoriesListState.value?.copy(dataSet = newDataSet)
    }

    data class CategoriesListState (
        val dataSet: List<Category> = emptyList(),
    )
}