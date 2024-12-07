package com.sol.recipeapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.RETROFIT_MEDIA_TYPE
import com.sol.recipeapp.data.RetrofitInstance.service
import com.sol.recipeapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val recipesDao: RecipesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            categoriesDao.getCategories()
        }
    }

    suspend fun insertCategoriesFromCache(categories: List<Category>?) {
        if (categories != null) {
            withContext(ioDispatcher) {
                categoriesDao.insertCategories(categories)
            }
        }
    }

    suspend fun getRecipesFromCacheByCategoryId(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(categoryId)
        }
    }

    suspend fun insertRecipesFromCache(recipes: List<Recipe>?) {
        if (recipes != null) {
            withContext(ioDispatcher) {
                recipesDao.insertRecipes(recipes)
            }
        }
    }

    suspend fun getCategorySync(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                val response = service.getCategories().execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("!!!error", "Response category ${response.isSuccessful}")
                    Log.e("!!!error", "Response body ${response.body()}")
                    null
                }

            } catch (e: IOException) {
                Log.e("!!!error", "Ошибка сети ${e.message}")
                null
            } catch (e: Exception) {
                Log.e("!!!error", "Неизвестная ошибка ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRecipesByCategoryIdSync(id: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                withContext(ioDispatcher) {
                    val response = service.getRecipesByCategoryId(id).execute()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Log.e("!!!error", "Response recipe by category id ${response.isSuccessful}")
                        Log.e("!!!error", "Response body ${response.body()}")
                        null
                    }
                }
            } catch (e: IOException) {
                Log.e("!!!error", "Ошибка сети ${e.message}")
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getCategoryByIdSync(id: Int): Category? {
        return withContext(ioDispatcher) {
            try {
                withContext(ioDispatcher) {
                    val response = service.getCategoryById(id).execute()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Log.e("!!!error", "Response category by id ${response.isSuccessful}")
                        Log.e("!!!error", "Response body ${response.body()}")
                        null
                    }
                }
            } catch (e: IOException) {
                Log.e("!!!error", "Ошибка сети ${e.message}")
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRecipeByIdSync(id: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                withContext(ioDispatcher) {
                    val response = service.getRecipeById(id).execute()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Log.e("!!!error", "Response recipe by id ${response.isSuccessful}")
                        Log.e("!!!error", "Response body ${response.body()}")
                        null
                    }
                }
            } catch (e: IOException) {
                Log.e("!!!error", "Ошибка сети", e)
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRecipesByIdsSync(setIds: Set<Int>): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                withContext(ioDispatcher) {
                    val stringIds = setIds.joinToString(",")
                    val response = service.getRecipesById(stringIds).execute()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        Log.e("!!!error", "Response recipe by ids ${response.isSuccessful}")
                        Log.e("!!!error", "Response body ${response.body()}")
                        null
                    }
                }
            } catch (e: IOException) {
                Log.e("!!!error", "Ошибка сети ${e.message}")
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
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
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(Json.asConverterFactory(converterType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
}