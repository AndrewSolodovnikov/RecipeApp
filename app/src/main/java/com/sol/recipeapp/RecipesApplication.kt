package com.sol.recipeapp

import android.app.Application
import com.sol.recipeapp.di.AppContainer

class RecipesApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}