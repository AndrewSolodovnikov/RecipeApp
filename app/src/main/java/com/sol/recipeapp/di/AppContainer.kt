package com.sol.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.RETROFIT_MEDIA_TYPE
import com.sol.recipeapp.createOkHttpClient
import com.sol.recipeapp.data.AppDatabase
import com.sol.recipeapp.data.FavoritesRepository
import com.sol.recipeapp.data.RecipeApiService
import com.sol.recipeapp.data.RecipesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database",
    ).build()

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.recipesDao()
    private val favoritesDao = db.favoritesDao()

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val converterType: MediaType = RETROFIT_MEDIA_TYPE.toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(Json.asConverterFactory(converterType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val recipesRepository = RecipesRepository(
        ioDispatcher = ioDispatcher,
        categoriesDao = categoriesDao,
        recipesDao = recipesDao,
    )

    private val favoritesRepository = FavoritesRepository(
        ioDispatcher = ioDispatcher,
        favoritesDao = favoritesDao,
    )

    val categoryListViewModelFactory = CategoriesListViewModelFactory(recipesRepository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(favoritesRepository)
    val recipeListViewModelFactory = RecipeListViewModelFactory(recipesRepository)
    val recipeViewModelFactory = RecipeViewModelFactory(favoritesRepository)
}