package com.sol.recipeapp.data

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val favoritesDao: FavoritesDao,
    //context: Context
) {
    //private val recipesDatabase: AppDatabase = AppDatabase.getDatabase(context)
    //private val favoritesDao: FavoritesDao = recipesDatabase.favoritesDao()

    suspend fun getFavoriteFromCache(recipeId: Int): Int {
        return withContext(ioDispatcher) {
            favoritesDao.getFavorite(recipeId)
        }
    }

    suspend fun getFavoritesListFromCache(): List<Recipe> {
        return withContext(ioDispatcher) {
            favoritesDao.getFavoritesList()
        }
    }

    suspend fun updateFavoriteFromCache(recipeId: Int, isFavorite: Int) {
        withContext(ioDispatcher) {
            favoritesDao.updateFavorite(recipeId, isFavorite)
        }
    }
}