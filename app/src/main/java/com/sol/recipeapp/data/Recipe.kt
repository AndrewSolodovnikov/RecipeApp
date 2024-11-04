package com.sol.recipeapp.data

import android.os.Parcelable
import com.sol.recipeapp.data.Ingredient
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
): Parcelable