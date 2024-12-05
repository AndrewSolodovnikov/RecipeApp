package com.sol.recipeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Category::class, Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
    abstract fun favoritesDao(): FavoritesDao

}
