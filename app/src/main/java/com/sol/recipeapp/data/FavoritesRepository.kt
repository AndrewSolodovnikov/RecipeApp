package com.sol.recipeapp.data

import com.sol.recipeapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val favoritesDao: FavoritesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

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