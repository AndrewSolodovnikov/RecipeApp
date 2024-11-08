package com.sol.recipeapp.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.RecipesRepository
import java.util.concurrent.ExecutorService

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val executorService: ExecutorService by lazy {
        (application as MyApplication).executorService
    }
    private val repository = RecipesRepository(application)

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState = _categoriesListState

    init {
        executorService.submit{
            try {
                val category = repository.getCategorySync()
                Log.i("!!!toast", "category $category")

                if (category != null) {
                    _categoriesListState.postValue(
                        _categoriesListState.value?.copy(dataSet = category)
                    )
                } else {
                    _categoriesListState.postValue(
                        _categoriesListState.value?.copy(errorMessage = "Ошибка получения данных")
                    )
                    Log.i("!!!toast", "value ${_categoriesListState.value?.errorMessage}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _categoriesListState.postValue(
                    _categoriesListState.value?.copy(errorMessage = "Ошибка получения данных")
                )
            }
        }

    }

    data class CategoriesListState(
        val dataSet: List<Category> = emptyList(),
        val errorMessage: String? = null,
    )
}