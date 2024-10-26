package com.sol.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i("!!!info", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        threadPool.execute {
            Log.i("!!!info", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val body = connection.inputStream.bufferedReader().readText()
            Log.i("!!!info", "responseCode = ${connection.responseCode}")
            Log.i("!!!info", "responseMessage = ${connection.responseMessage}")
            Log.i("!!!info", "Body = $body")

            val categoryObject: List<Category> = Json.decodeFromString(body)
            Log.i("!!!info", "kotlinx serialization = $categoryObject")

            val categoryListType = object : TypeToken<List<Category>>() {}.type
            val categoryObjectGson = Gson().fromJson<List<Category>>(body, categoryListType)
            Log.i("!!!info", "Gson serialization = $categoryObjectGson")

            val listIdsCategory: List<Int> = categoryObject.map { it.id }
            Log.i("!!!info", "List ids category = $listIdsCategory")

            listIdsCategory.forEach { id ->
                threadPool.execute {
                    val categoryRecipesUrl = URL(
                        "https://recipes.androidsprint.ru/api/category/$id/recipes"
                    )
                    val connectionCategoryRecipes =
                        categoryRecipesUrl.openConnection() as HttpURLConnection
                    connectionCategoryRecipes.connect()

                    val bodyCategoryRecipe = connectionCategoryRecipes
                        .inputStream.bufferedReader().readText()
                    Log.i(
                        "!!!info",
                        "responseCode category recipes [$id] = ${connectionCategoryRecipes.responseCode}"
                    )
                    Log.i(
                        "!!!info",
                        "responseMessage category recipes [$id] = ${connectionCategoryRecipes.responseMessage}"
                    )
                    Log.i("!!!info", "Body category recipes [$id] = $bodyCategoryRecipe")
                    Log.i("!!!info", "Name running thread = ${Thread.currentThread().name}")
                }
            }
        }

        binding.navigationButtonCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.categoriesListFragment, null, navOptions)
        }

        binding.navigationButtonFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment_container)
                .navigate(R.id.favoritesFragment, null, navOptions)
        }
    }
}