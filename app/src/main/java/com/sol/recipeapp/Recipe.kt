package com.sol.recipeapp

data class Recipe(
    val id: Int,
    val title: String,
    val ingredient: List<Ingredient>,
)
