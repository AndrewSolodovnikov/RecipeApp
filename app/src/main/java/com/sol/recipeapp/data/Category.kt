package com.sol.recipeapp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val description: String,
    @ColumnInfo val imageUrl: String,
) : Parcelable
