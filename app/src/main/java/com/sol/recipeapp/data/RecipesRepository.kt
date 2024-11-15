package com.sol.recipeapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.RETROFIT_BASE_URL
import com.sol.recipeapp.RETROFIT_MEDIA_TYPE
import com.sol.recipeapp.data.RetrofitInstance.service
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

class RecipesRepository {

    fun getCategorySync(): List<Category>? {
        return try {
            val response = service.getCategories().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: IOException) {
            Log.e("Error", "Ошибка сети ${e.message}")
            null
        } catch (e: HttpException) {
            Log.e("Error", "Ошибка сервера ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("Error", "Неизвестная ошибка ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getRecipesByCategoryIdSync(id: Int): List<Recipe>? {
        return try {
            val response = service.getRecipesByCategoryId(id).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: IOException) {
            Log.e("Error", "Ошибка сети ${e.message}")
            null
        } catch (e: HttpException) {
            Log.e("Error", "Ошибка сервера ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("Error", "Неизвестная ошибка ${e.message}")
            e.printStackTrace()
            null
        }
    }

    fun getCategoryByIdSync(id: Int): Category? {
        return try {
            val response = service.getCategoryById(id).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: IOException) {
            Log.e("Error", "Ошибка сети ${e.message}")
            null
        } catch (e: HttpException) {
            Log.e("Error", "Ошибка сервера ${e.message}")
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getRecipeByIdSync(id: Int): Recipe? {
        return try {
            val response = service.getRecipeById(id).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: IOException) {
            Log.e("Error", "Ошибка сети", e)
            null
        } catch (e: HttpException) {
            Log.e("Error", "Ошибка сервера", e)
            null
        } catch (e: Exception) {
            Log.e("Error", "Неизвестная ошибка", e)
            e.printStackTrace()
            null
        }
    }

    fun getRecipesByIdsSync(setIds: Set<Int>): List<Recipe>? {
        return try {
            val stringIds = setIds.joinToString(",")
            val response = service.getRecipesById(stringIds).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: IOException) {
            Log.e("Error", "Ошибка сети ${e.message}")
            null
        } catch (e: HttpException) {
            Log.e("Error", "Ошибка сервера ${e.message}")
            null
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