package com.sol.recipeapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
): Parcelable