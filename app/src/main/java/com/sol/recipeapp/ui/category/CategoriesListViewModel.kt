package com.sol.recipeapp.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.data.RecipesRepository
import com.sol.recipeapp.data.ThreadPool
import java.util.concurrent.ExecutorService

class CategoriesListViewModel(app: Application) : AndroidViewModel(app) {
    private val executorService: ExecutorService by lazy {
        (app as MyApplication).executorService
    }
    private val repository = RecipesRepository()

    private val _categoriesListState = MutableLiveData(CategoriesListState())
    val categoriesListState = _categoriesListState

    init {
        executorService.submit{
            try {
                val newDataSet = repository.getCategorySync()
                _categoriesListState.value = newDataSet?.let {
                    _categoriesListState.value?.copy(dataSet = it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


        Log.i("!!!info", "Good job!!!")


    }

    data class CategoriesListState(
        val dataSet: List<Category> = emptyList(),
    )
}