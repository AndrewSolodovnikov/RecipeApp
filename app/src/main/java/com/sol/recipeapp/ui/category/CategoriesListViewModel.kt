package com.sol.recipeapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.STUB
import com.sol.recipeapp.data.Category

class CategoriesListViewModel(application: Application): AndroidViewModel(application) {
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