package com.sol.recipeapp.data

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.com.sol.recipeapp.MyApplication
import com.sol.recipeapp.data.RetrofitInstance.service
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository(application: Application) {
    private val executorService = (application as MyApplication).executorService

    fun getCategorySync(): List<Category>? {
        return try {
            executorService.submit<List<Category>?> {
                val response = service.getCategories().execute()
                if (response.isSuccessful) response.body() else null
            }.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recipesByCategoryIdSync(id: Int): List<Recipe>? {
        Log.i("!!!info", "Start recipesByCategoryIdSync")
        return try {
            executorService.submit<List<Recipe>?> {
                Log.i("!!!info", "Start thread recipesByCategoryIdSync")
                val response = service.getRecipesByCategoryId(id).execute()
                Log.i("!!!info", "Response $response")
                if (response.isSuccessful) response.body() else null
            }.get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun categoryByIdSync(id: Int): Category? {
        return try {
            executorService.submit<Category> {
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
            executorService.submit<Recipe> {
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