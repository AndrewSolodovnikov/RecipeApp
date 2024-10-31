package com.sol.recipeapp.data

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.RetrofitInstance.service
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RecipesRepository {
    //private val executorService = (app as MyApplication).executorService

    fun getCategorySync(): List<Category>? {
        return try {
            ThreadPool.executorService.submit<List<Category>?> {
                val response = service.getCategories().execute()
                if (response.isSuccessful) response.body() else null
            }.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recipesByCategoryIdSync(id: String): List<Recipe>? {
        return try {
            ThreadPool.executorService.submit<List<Recipe>?> {
                val response = service.getRecipesByCategoryId(id).execute()
                if (response.isSuccessful) response.body() else null
            }.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun categoryByIdSync(id: String): Category? {
        return try {
            ThreadPool.executorService.submit<Category> {
                val response = service.getCategoryById(id).execute()
                if (response.isSuccessful) response.body() else null
            }.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recipeByIdSync(id: String): Recipe? {
        return try {
            ThreadPool.executorService.submit<Recipe> {
                val response = service.getRecipeById(id).execute()
                if (response.isSuccessful) response.body() else null
            }.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

object RetrofitInstance {
    private val converterType: MediaType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(converterType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
}

object ThreadPool {
    val executorService: ExecutorService = Executors.newFixedThreadPool(10)
}
