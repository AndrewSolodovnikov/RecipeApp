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
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i("!!!info", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        thread {
            Log.i("!!!info","Выполняю запрос на потоке: ${Thread.currentThread().name}")
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