package com.sol.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sol.recipeapp.BASE_URL
import com.sol.recipeapp.RETROFIT_MEDIA_TYPE
import com.sol.recipeapp.createOkHttpClient
import com.sol.recipeapp.data.AppDatabase
import com.sol.recipeapp.data.CategoriesDao
import com.sol.recipeapp.data.FavoritesDao
import com.sol.recipeapp.data.RecipeApiService
import com.sol.recipeapp.data.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database",
        ).build()

    @Provides
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoriesDao = appDatabase.categoriesDao()

    @Provides
    fun providesRecipesDao(appDatabase: AppDatabase): RecipesDao = appDatabase.recipesDao()

    @Provides
    fun providesFavoritesDao(appDatabase: AppDatabase): FavoritesDao = appDatabase.favoritesDao()

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        val converterType: MediaType = RETROFIT_MEDIA_TYPE.toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(Json.asConverterFactory(converterType))
            .build()
        return retrofit
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)

    }

}