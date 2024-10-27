package com.sol.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.sol.recipeapp.data.Category
import com.sol.recipeapp.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
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
    private lateinit var categoryObject: List<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i("!!!info", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        threadPool.execute {
            val client = createOkHttpClient()
            val body = getJson("https://recipes.androidsprint.ru/api/category", client)

            body?.let {
                categoryObject = Json.decodeFromString(it)
                Log.i("!!!info", "kotlinx serialization = $categoryObject")
            }

            val listIdsCategory: List<Int> = categoryObject.map { it.id }
            Log.i("!!!info", "List ids category = $listIdsCategory")

            listIdsCategory.forEach { id ->
                threadPool.execute {
                    val bodyCategoryRecipe: String? = getJson(
                        "https://recipes.androidsprint.ru/api/category/$id/recipes", client
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

fun getJson(targetUrl: String, client: OkHttpClient): String? {
    val request: Request = Request.Builder()
        .url(targetUrl)
        .build()

    return client.newCall(request).execute().body?.string()
}

fun createOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}