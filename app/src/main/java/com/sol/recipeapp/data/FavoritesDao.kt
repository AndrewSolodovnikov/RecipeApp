package com.sol.recipeapp.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavoritesDao {
    @Query("SELECT isFavorite FROM recipe WHERE id = :recipeId")
    suspend fun getFavorite(recipeId: Int): Int

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavoritesList(): List<Recipe>

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavorite(recipeId: Int, isFavorite: Int)
}