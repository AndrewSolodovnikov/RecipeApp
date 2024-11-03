package com.sol.recipeapp.ui.category

import android.app.Application
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
                repository.getCategorySync()?.let {
                    _categoriesListState.postValue(
                        _categoriesListState.value?.copy(dataSet = it)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }





    }

    data class CategoriesListState(
        val dataSet: List<Category> = emptyList(),
    )
}