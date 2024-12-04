package com.sol.recipeapp.di

interface Factory<T> {
    fun create(): T
}