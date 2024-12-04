package com.sol.recipeapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val favoritesDao: FavoritesDao,
) {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

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