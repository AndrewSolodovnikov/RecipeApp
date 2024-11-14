package com.sol.recipeapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.RETROFIT_BASE_URL
import com.sol.recipeapp.RETROFIT_MEDIA_TYPE
import com.sol.recipeapp.data.RetrofitInstance.service
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository {

    fun getCategorySync(): List<Category>? {
        return try {
                val response = service.getCategories().execute()
                if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recipesByCategoryIdSync(id: Int): List<Recipe>? {
        return try {
                val response = service.getRecipesByCategoryId(id).execute()
                if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun categoryByIdSync(id: Int): Category? {
        return try {
                val response = service.getCategoryById(id).execute()
                if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recipeByIdSync(id: Int): Recipe? {
        return try {
                val response = service.getRecipeById(id).execute()
                if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun recipesByIdSync(id: String): List<Recipe>? {
        return try {
                val response = service.getRecipesById(id).execute()
                if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

private fun createOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

object RetrofitInstance {
    private val converterType: MediaType = RETROFIT_MEDIA_TYPE.toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(RETROFIT_BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(Json.asConverterFactory(converterType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
}