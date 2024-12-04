package com.sol.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.sol.recipeapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

@AndroidEntryPoint
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