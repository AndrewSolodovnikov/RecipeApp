package com.sol.recipeapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("/category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: String?): Call<List<Recipe>>

    @GET("/category/{id}")
    fun getCategoryById(@Path("id") id: String?): Call<Category>

    @GET("recipes?ids={id}")
    fun getRecipeById(@Query("id") id: String?): Call<Recipe>

}