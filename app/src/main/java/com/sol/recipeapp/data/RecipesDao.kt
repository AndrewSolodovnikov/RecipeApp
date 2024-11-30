package com.sol.recipeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT isFavorite FROM recipe WHERE id = :recipeId")
    suspend fun getFavorite(recipeId: Int): Int

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavoritesList(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavorite(recipeId: Int, isFavorite: Int)
}