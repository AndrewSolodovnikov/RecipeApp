package com.sol.recipeapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity
data class Recipe(
    @PrimaryKey val id: Int,
    val categoryId: Int? = -1,
    val title: String,
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val imageUrl: String,
): Parcelable