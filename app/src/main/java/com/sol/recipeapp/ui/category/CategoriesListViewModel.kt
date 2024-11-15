package com.sol.recipeapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.RecipesRepository
import java.util.concurrent.ExecutorService

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val executorService: ExecutorService by lazy {
        (application as MyApplication).executorService
    }
    private val repository = RecipesRepository()

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState: LiveData<CategoriesListState> = _categoriesListState

    init {
        executorService.submit {
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